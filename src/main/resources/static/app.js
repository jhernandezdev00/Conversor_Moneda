const API = {
    currencies: '/api/currencies',
    convert: '/api/convert',
};
const LOCALE = 'es-MX';
const DEBOUNCE_MS = 250;
const COPY_FEEDBACK_MS = 1500;
const STORAGE_KEY = 'conversor:preferencias';
const AMOUNT_PATTERN = /^\d{0,13}(\.\d{0,6})?$/;
const DEFAULTS = { from: 'USD', to: 'MXN' };

const el = {
    form: document.getElementById('converter-form'),
    amount: document.getElementById('amount'),
    amountError: document.getElementById('amount-error'),
    from: document.getElementById('from'),
    to: document.getElementById('to'),
    swap: document.getElementById('swap'),
    resultSource: document.getElementById('result-source'),
    resultValue: document.getElementById('result-value'),
    rate: document.getElementById('rate'),
    inverseRate: document.getElementById('inverse-rate'),
    copy: document.getElementById('copy'),
    statusText: document.getElementById('status-text'),
    retry: document.getElementById('retry'),
};

const state = {
    currenciesLoaded: false,
    debounceTimer: null,
    copyTimer: null,
    controller: null,
    lastResult: '',
};

const dateFormat = new Intl.DateTimeFormat(LOCALE, { dateStyle: 'medium', timeStyle: 'short' });

class ApiError extends Error {
    constructor(message, code, status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}

async function getJson(url, signal) {
    let response;
    try {
        response = await fetch(url, { signal });
    } catch (error) {
        if (error.name === 'AbortError') {
            throw error;
        }
        throw new ApiError('No hay conexión con el servidor. Revisa tu conexión a internet.', 'NETWORK', 0);
    }

    if (!response.ok) {
        const problem = await response.json().catch(() => ({}));
        throw new ApiError(
            problem.detail ?? `Ocurrió un error inesperado (HTTP ${response.status}).`,
            problem.code ?? 'UNKNOWN',
            response.status,
        );
    }
    return response.json();
}

async function loadCurrencies() {
    setStatus('Cargando tasas de cambio…');
    try {
        const data = await getJson(API.currencies);
        const saved = loadPreferences();
        fillSelect(el.from, data.currencies, saved.from ?? DEFAULTS.from);
        fillSelect(el.to, data.currencies, saved.to ?? DEFAULTS.to);
        setControlsEnabled(true);
        state.currenciesLoaded = true;
        showUpdated(data.lastUpdate);
        scheduleConversion(0);
    } catch (error) {
        showError(error);
    }
}

function fillSelect(select, currencies, preferredCode) {
    const options = currencies.map(c => new Option(`${c.code} — ${c.name}`, c.code));
    select.replaceChildren(...options);
    const hasPreferred = currencies.some(c => c.code === preferredCode);
    select.value = hasPreferred ? preferredCode : currencies[0]?.code ?? '';
}

function setControlsEnabled(enabled) {
    el.from.disabled = !enabled;
    el.to.disabled = !enabled;
    el.swap.disabled = !enabled;
}

function readAmount() {
    const raw = el.amount.value.replace(/[,\s$]/g, '');
    if (raw === '' || raw === '.') {
        return { empty: true };
    }
    if (!AMOUNT_PATTERN.test(raw)) {
        return { error: 'Escribe un número válido, con hasta 13 dígitos enteros y 6 decimales. Ejemplo: 1500.50' };
    }
    return { value: raw };
}

function scheduleConversion(delay = DEBOUNCE_MS) {
    clearTimeout(state.debounceTimer);
    state.debounceTimer = setTimeout(convert, delay);
}

async function convert() {
    if (!state.currenciesLoaded) {
        return;
    }
    state.controller?.abort();
    savePreferences();

    const amount = readAmount();
    if (amount.error) {
        setAmountError(amount.error);
        showPlaceholder();
        return;
    }
    setAmountError(null);
    if (amount.empty) {
        showPlaceholder();
        return;
    }

    const controller = new AbortController();
    state.controller = controller;
    const params = new URLSearchParams({ from: el.from.value, to: el.to.value, amount: amount.value });

    try {
        const conversion = await getJson(`${API.convert}?${params}`, controller.signal);
        showResult(conversion);
        showUpdated(conversion.lastUpdate);
    } catch (error) {
        if (error.name === 'AbortError') {
            return;
        }
        showPlaceholder();
        if (error.code === 'INVALID_AMOUNT') {
            setAmountError(error.message);
        } else {
            showError(error);
        }
    }
}

function swapCurrencies() {
    const from = el.from.value;
    el.from.value = el.to.value;
    el.to.value = from;
    el.swap.classList.toggle('is-swapped');
    scheduleConversion(0);
}

async function copyResult() {
    if (!state.lastResult) {
        return;
    }
    let label = 'Copiado';
    try {
        await navigator.clipboard.writeText(state.lastResult);
    } catch {
        label = 'No se pudo copiar';
    }
    clearTimeout(state.copyTimer);
    el.copy.textContent = label;
    state.copyTimer = setTimeout(() => {
        el.copy.textContent = 'Copiar resultado';
    }, COPY_FEEDBACK_MS);
}

function showResult(c) {
    const fromDigits = currencyDigits(c.from);
    const toDigits = currencyDigits(c.to);
    state.lastResult = formatNumber(c.result, toDigits);

    el.resultSource.textContent = `${formatNumber(c.amount, fromDigits)} ${c.from} =`;
    el.resultValue.textContent = `${state.lastResult} ${c.to}`;
    el.rate.textContent = `1 ${c.from} = ${formatNumber(c.rate, 4)} ${c.to}`;
    el.inverseRate.textContent = `1 ${c.to} = ${formatNumber(c.inverseRate, 4)} ${c.from}`;
    el.copy.disabled = false;
}

function showPlaceholder() {
    state.lastResult = '';
    el.resultSource.textContent = 'Escribe un importe para convertir';
    el.resultValue.textContent = '—';
    el.rate.textContent = '';
    el.inverseRate.textContent = '';
    el.copy.disabled = true;
}

function setAmountError(message) {
    const hasError = Boolean(message);
    el.amount.classList.toggle('is-invalid', hasError);
    el.amount.setAttribute('aria-invalid', String(hasError));
    el.amountError.textContent = message ?? '';
}

function showUpdated(isoDate) {
    setStatus(`Tasas actualizadas: ${dateFormat.format(new Date(isoDate))}`);
}

function showError(error) {
    const message = error instanceof ApiError ? error.message : 'Ocurrió un error inesperado.';
    const canRetry = !(error instanceof ApiError) || error.status === 0 || error.status >= 500;
    setStatus(message, { isError: true, canRetry });
    if (!(error instanceof ApiError)) {
        console.error(error);
    }
}

function setStatus(message, { isError = false, canRetry = false } = {}) {
    el.statusText.textContent = message;
    el.statusText.classList.toggle('text-danger', isError);
    el.statusText.classList.toggle('text-body-secondary', !isError);
    el.retry.hidden = !canRetry;
}

function loadPreferences() {
    try {
        const saved = JSON.parse(localStorage.getItem(STORAGE_KEY)) ?? {};
        return {
            from: typeof saved.from === 'string' ? saved.from : undefined,
            to: typeof saved.to === 'string' ? saved.to : undefined,
            amount: typeof saved.amount === 'string' ? saved.amount : undefined,
        };
    } catch {
        return {};
    }
}

function savePreferences() {
    try {
        localStorage.setItem(STORAGE_KEY, JSON.stringify({
            from: el.from.value,
            to: el.to.value,
            amount: el.amount.value,
        }));
    } catch {
        // Si el navegador bloquea el almacenamiento, la app sigue funcionando sin recordar.
    }
}

function currencyDigits(code) {
    try {
        return new Intl.NumberFormat(LOCALE, { style: 'currency', currency: code })
            .resolvedOptions().maximumFractionDigits;
    } catch {
        return 2;
    }
}

function formatNumber(value, minDigits) {
    const maxDigits = Math.max(minDigits, significantDigitsFor(value));
    return new Intl.NumberFormat(LOCALE, {
        minimumFractionDigits: minDigits,
        maximumFractionDigits: maxDigits,
    }).format(value);
}

function significantDigitsFor(value) {
    const abs = value.replace('-', '');
    if (!abs.startsWith('0.')) {
        return 0;
    }
    const leadingZeros = abs.slice(2).match(/^0*/)[0].length;
    return Math.min(leadingZeros + 3, 10);
}

el.form.addEventListener('submit', event => event.preventDefault());
el.amount.addEventListener('input', () => scheduleConversion());
el.amount.addEventListener('focus', () => el.amount.select());
el.from.addEventListener('change', () => scheduleConversion(0));
el.to.addEventListener('change', () => scheduleConversion(0));
el.swap.addEventListener('click', swapCurrencies);
el.copy.addEventListener('click', copyResult);
el.retry.addEventListener('click', () => {
    if (state.currenciesLoaded) {
        scheduleConversion(0);
    } else {
        loadCurrencies();
    }
});

const initialAmount = loadPreferences().amount;
if (initialAmount !== undefined) {
    el.amount.value = initialAmount;
}
loadCurrencies();