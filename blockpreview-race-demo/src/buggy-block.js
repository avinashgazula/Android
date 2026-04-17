/**
 * BuggyBlock — mirrors BlockPreview's buggy behaviour.
 *
 * handleWorkspaceData() fires both:
 *   this.observeBlockValue();          // kicks off renderBlockPreview() — NOT awaited
 *   await this.fetchAndLoadStylesheets(); // loads CSS into shadow root
 *
 * The two run concurrently. On fast machines the API response arrives first
 * and markup is painted before the stylesheet is adopted → unstyled block.
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

// Simulated "API" response time — fast, like a local Umbraco server.
const API_RESPONSE_MS = 60;

class BuggyBlock extends HTMLElement {
  #shadow;
  #stylesAdopted = false;
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
    this.#fetchAndLoadStylesheets(); // intentionally NOT awaited → race begins
    this.#renderBlockPreview();      // runs concurrently
  }

  // ── Mirrors fetchAndLoadStylesheets() ─────────────────────────────────────
  #fetchAndLoadStylesheets() {
    if (this.#stylesAdopted) return Promise.resolve();
    this.#log('css', `CSS fetch started (${this.#delay} ms simulated delay)…`);
    return new Promise(resolve => {
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

  // ── Mirrors renderBlockPreview() ──────────────────────────────────────────
  async #renderBlockPreview() {
    // Simulate async API call for preview HTML
    await new Promise(r => setTimeout(r, API_RESPONSE_MS));

    // Set markup — no check whether stylesheets are ready
    this.#shadow.innerHTML = BLOCK_HTML;
    this.#log('render', 'Markup set on shadow root');

    if (this.#stylesAdopted) {
      this.#log('ok', '✓ Styles already adopted — lucky timing');
    } else {
      this.#log('warn', '⚠ Styles NOT yet adopted — FOUC! (race lost)');
    }
  }

  // ── Log helper ────────────────────────────────────────────────────────────
  #log(type, msg) {
    const log = document.getElementById('buggy-log');
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
    this.#shadow.innerHTML = '';
    this.#shadow.adoptedStyleSheets = [];
    this.#t0 = performance.now();
    this.#run();
  }
}

customElements.define('buggy-block', BuggyBlock);
