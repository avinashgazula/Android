/**
 * FixedBlock — the corrected version.
 *
 * Two changes vs BuggyBlock:
 *
 * 1. fetchAndLoadStylesheets() stores a single deduplicated Promise so
 *    concurrent callers all await the same fetch instead of spawning duplicates.
 *
 * 2. renderBlockPreview() awaits fetchAndLoadStylesheets() before setting
 *    _htmlMarkup, guaranteeing the shadow root already has its stylesheet
 *    when Lit renders the markup.
 */

const BLOCK_CSS = `
  .block-card {
    border: 2px solid #3544b1;
    border-radius: 8px;
    padding: 1.25rem 1.5rem;
    background: linear-gradient(135deg, #3544b1 0%, #6c63ff 100%);
    color: white;
  }
  .block-card h2 {
    margin: 0 0 0.4rem;
    font-size: 1.1rem;
    font-family: system-ui, sans-serif;
  }
  .block-card p {
    margin: 0 0 0.75rem;
    font-size: 0.875rem;
    font-family: system-ui, sans-serif;
    opacity: 0.9;
  }
  .block-card .tag {
    display: inline-block;
    background: rgba(255,255,255,0.2);
    padding: 0.2rem 0.65rem;
    border-radius: 20px;
    font-size: 0.75rem;
    font-family: system-ui, sans-serif;
  }
`;

const BLOCK_HTML = `
  <div class="block-card">
    <h2>Hero Block</h2>
    <p>Preview rendered by the BlockPreview API.</p>
    <span class="tag">Umbraco.BlockGrid</span>
  </div>
`;

const LOADING_HTML = `
  <p style="font-family:system-ui,sans-serif;font-size:0.85rem;color:#666;margin:0">
    ⏳ Loading preview…
  </p>
`;

const API_RESPONSE_MS = 60;

class FixedBlock extends HTMLElement {
  #shadow;
  #stylesAdopted = false;
  #stylesheetPromise = null; // deduplicated — multiple callers share one fetch
  #delay = 800;
  #t0 = 0;

  constructor() {
    super();
    this.#shadow = this.attachShadow({ mode: 'open' });
  }

  connectedCallback() {
    this.#t0 = performance.now();
    this.#delay = parseInt(this.getAttribute('delay') ?? '800', 10);
    this.#run();
  }

  // ── Mirrors handleWorkspaceData() ──────────────────────────────────────────
  #run() {
    // Kick off the stylesheet fetch early so it can run in parallel with the
    // API call inside renderBlockPreview() — but renderBlockPreview() will
    // await it before touching the DOM.
    this.#fetchAndLoadStylesheets();
    this.#renderBlockPreview();
  }

  // ── FIX: deduplicated promise; safe to call from multiple sites ───────────
  #fetchAndLoadStylesheets() {
    if (this.#stylesAdopted) return Promise.resolve();

    if (!this.#stylesheetPromise) {
      this.#log('css', `CSS fetch started (${this.#delay} ms simulated delay)…`);

      this.#stylesheetPromise = new Promise(resolve => {
        setTimeout(() => {
          const sheet = new CSSStyleSheet();
          sheet.replaceSync(BLOCK_CSS);
          this.#shadow.adoptedStyleSheets = [sheet];
          this.#stylesAdopted = true;
          this.#log('css', 'Stylesheet adopted into shadow root');
          resolve();
        }, this.#delay);
      });
    }

    return this.#stylesheetPromise;
  }

  // ── FIX: await stylesheets before setting markup ──────────────────────────
  async #renderBlockPreview() {
    this.#shadow.innerHTML = LOADING_HTML;

    // Run both in parallel, but don't touch the DOM until BOTH are done.
    const [, html] = await Promise.all([
      this.#fetchAndLoadStylesheets(),
      this.#simulateApiCall(),
    ]);

    // At this point the stylesheet is guaranteed to be in the shadow root.
    this.#shadow.innerHTML = html;
    this.#log('render', 'Markup set — stylesheet already adopted');
    this.#log('ok', '✓ Rendered with styles (race prevented)');
  }

  async #simulateApiCall() {
    this.#log('api', `API call started (${API_RESPONSE_MS} ms)…`);
    await new Promise(r => setTimeout(r, API_RESPONSE_MS));
    this.#log('api', 'API returned preview HTML');
    return BLOCK_HTML;
  }

  // ── Log helper ────────────────────────────────────────────────────────────
  #log(type, msg) {
    const log = document.getElementById('fixed-log');
    if (!log) return;
    const el = document.createElement('div');
    el.className = `log-entry log-${type}`;
    el.textContent = `[+${(performance.now() - this.#t0).toFixed(0)} ms] ${msg}`;
    log.appendChild(el);
  }

  // ── Public reset (called by the Re-run button) ───────────────────────────
  reset(delay) {
    this.#delay = delay;
    this.#stylesAdopted = false;
    this.#stylesheetPromise = null;
    this.#shadow.innerHTML = '';
    this.#shadow.adoptedStyleSheets = [];
    this.#t0 = performance.now();
    this.#run();
  }
}

customElements.define('fixed-block', FixedBlock);
