/**
 * SIMR — Smart Inventory, Supplier & Predictive Reorder Management Platform
 * Next-Gen Futuristic Cyber-Glassmorphic Application Engine
 * Dual-Mode (Java 21 REST API + Standalone Local Storage Fallback)
 */

// =============================================================================
// 1. DEFAULT RICH SEED DATASET
// =============================================================================
const DEFAULT_SUPPLIERS = [
    { id: 1, supplierCode: 'SUP-001', name: 'ABC Electronics Ltd', contactPerson: 'Robert Miller', phone: '+1-555-0192', email: 'robert@abcelectronics.com', address: '104 Industrial Ave, Silicon Bay, CA', leadTime: 3, reliabilityScore: 96, status: 'Active', productsCount: 5 },
    { id: 2, supplierCode: 'SUP-002', name: 'TechWorld Components', contactPerson: 'Sarah Jenkins', phone: '+1-555-0144', email: 'sales@techworldcomp.com', address: '240 Tech Parkway, Austin, TX', leadTime: 4, reliabilityScore: 92, status: 'Active', productsCount: 4 },
    { id: 3, supplierCode: 'SUP-003', name: 'Global Traders Inc', contactPerson: 'David Zhang', phone: '+1-555-0178', email: 'dzhang@globaltraders.io', address: '55 Trade Center Blvd, Seattle, WA', leadTime: 5, reliabilityScore: 88, status: 'Active', productsCount: 2 },
    { id: 4, supplierCode: 'SUP-004', name: 'Apex Networking Solutions', contactPerson: 'Elena Rostova', phone: '+1-555-0129', email: 'elena@apexnet.com', address: '88 Connectivity Way, Boston, MA', leadTime: 2, reliabilityScore: 98, status: 'Active', productsCount: 3 },
    { id: 5, supplierCode: 'SUP-005', name: 'Nexus Office Supplies', contactPerson: 'Marcus Vance', phone: '+1-555-0163', email: 'marcus@nexusoffices.com', address: '312 Corporate Plaza, Chicago, IL', leadTime: 3, reliabilityScore: 94, status: 'Active', productsCount: 3 }
];

const DEFAULT_PRODUCTS = [
    { id: 1, productCode: 'PRD-1001', name: 'Pro Laptop 15.6" i7', category: 'Computer Equipment', supplierId: 3, supplierName: 'Global Traders Inc', unitPrice: 62500.00, currentStock: 25, minimumStock: 10, maximumStock: 50, reorderQuantity: 20, rackBay: 'C-1' },
    { id: 2, productCode: 'PRD-1002', name: 'Wireless Mechanical Keyboard', category: 'Accessories', supplierId: 1, supplierName: 'ABC Electronics Ltd', unitPrice: 3450.00, currentStock: 7, minimumStock: 10, maximumStock: 60, reorderQuantity: 40, rackBay: 'D-1' },
    { id: 3, productCode: 'PRD-1003', name: 'Ergonomic Optical Mouse', category: 'Accessories', supplierId: 2, supplierName: 'TechWorld Components', unitPrice: 1250.00, currentStock: 3, minimumStock: 12, maximumStock: 70, reorderQuantity: 50, rackBay: 'D-2' },
    { id: 4, productCode: 'PRD-1004', name: 'Ultra-Slim 27" 4K Monitor', category: 'Electronics', supplierId: 1, supplierName: 'ABC Electronics Ltd', unitPrice: 24999.00, currentStock: 18, minimumStock: 8, maximumStock: 40, reorderQuantity: 15, rackBay: 'A-1' },
    { id: 5, productCode: 'PRD-1005', name: 'USB-C 7-in-1 Multiport Hub', category: 'Accessories', supplierId: 1, supplierName: 'ABC Electronics Ltd', unitPrice: 2199.00, currentStock: 5, minimumStock: 10, maximumStock: 50, reorderQuantity: 30, rackBay: 'D-3' },
    { id: 6, productCode: 'PRD-1006', name: 'High-Speed HDMI 2.1 Cable (2m)', category: 'Accessories', supplierId: 2, supplierName: 'TechWorld Components', unitPrice: 499.00, currentStock: 0, minimumStock: 15, maximumStock: 100, reorderQuantity: 60, rackBay: 'D-4' },
    { id: 7, productCode: 'PRD-1007', name: 'Cat6 Gigabit Ethernet Cable (10m)', category: 'Networking', supplierId: 4, supplierName: 'Apex Networking Solutions', unitPrice: 349.00, currentStock: 8, minimumStock: 15, maximumStock: 120, reorderQuantity: 50, rackBay: 'B-1' },
    { id: 8, productCode: 'PRD-1008', name: 'Gigabit 16-Port Network Switch', category: 'Networking', supplierId: 4, supplierName: 'Apex Networking Solutions', unitPrice: 7850.00, currentStock: 14, minimumStock: 6, maximumStock: 30, reorderQuantity: 12, rackBay: 'B-2' },
    { id: 9, productCode: 'PRD-1009', name: 'Wi-Fi 6 Dual Band Router', category: 'Networking', supplierId: 4, supplierName: 'Apex Networking Solutions', unitPrice: 5490.00, currentStock: 22, minimumStock: 10, maximumStock: 45, reorderQuantity: 20, rackBay: 'B-3' },
    { id: 10, productCode: 'PRD-1010', name: '16GB DDR4 3200MHz RAM', category: 'Computer Equipment', supplierId: 2, supplierName: 'TechWorld Components', unitPrice: 3200.00, currentStock: 2, minimumStock: 12, maximumStock: 60, reorderQuantity: 35, rackBay: 'C-2' },
    { id: 11, productCode: 'PRD-1011', name: '1TB NVMe M.2 PCIe Gen4 SSD', category: 'Computer Equipment', supplierId: 2, supplierName: 'TechWorld Components', unitPrice: 6800.00, currentStock: 30, minimumStock: 10, maximumStock: 50, reorderQuantity: 25, rackBay: 'C-3' },
    { id: 12, productCode: 'PRD-1012', name: 'Noise Cancelling Headset USB', category: 'Electronics', supplierId: 1, supplierName: 'ABC Electronics Ltd', unitPrice: 4200.00, currentStock: 16, minimumStock: 8, maximumStock: 40, reorderQuantity: 20, rackBay: 'A-2' },
    { id: 13, productCode: 'PRD-1013', name: 'Full HD 1080p Web Camera', category: 'Electronics', supplierId: 1, supplierName: 'ABC Electronics Ltd', unitPrice: 2750.00, currentStock: 19, minimumStock: 8, maximumStock: 50, reorderQuantity: 25, rackBay: 'A-3' },
    { id: 14, productCode: 'PRD-1014', name: 'Ergonomic Memory Foam Mousepad', category: 'Office Supplies', supplierId: 5, supplierName: 'Nexus Office Supplies', unitPrice: 399.00, currentStock: 6, minimumStock: 12, maximumStock: 80, reorderQuantity: 40, rackBay: 'E-1' },
    { id: 15, productCode: 'PRD-1015', name: 'Adjustable Aluminum Laptop Stand', category: 'Office Supplies', supplierId: 5, supplierName: 'Nexus Office Supplies', unitPrice: 1650.00, currentStock: 28, minimumStock: 10, maximumStock: 50, reorderQuantity: 20, rackBay: 'E-2' },
    { id: 16, productCode: 'PRD-1016', name: 'Thermal Paste Syringe (4g)', category: 'Computer Equipment', supplierId: 2, supplierName: 'TechWorld Components', unitPrice: 450.00, currentStock: 4, minimumStock: 10, maximumStock: 50, reorderQuantity: 30, rackBay: 'C-4' },
    { id: 17, productCode: 'PRD-1017', name: 'Surge Protector 6-Socket Strip', category: 'Electronics', supplierId: 3, supplierName: 'Global Traders Inc', unitPrice: 1150.00, currentStock: 24, minimumStock: 10, maximumStock: 60, reorderQuantity: 25, rackBay: 'A-4' },
    { id: 18, productCode: 'PRD-1018', name: 'Heavy-Duty Cable Organizer Pack', category: 'Office Supplies', supplierId: 5, supplierName: 'Nexus Office Supplies', unitPrice: 299.00, currentStock: 45, minimumStock: 15, maximumStock: 100, reorderQuantity: 50, rackBay: 'E-3' }
];

function generateSeedTransactions() {
    const txs = [];
    let id = 1;
    const now = Date.now();
    const dayMs = 86400000;

    DEFAULT_PRODUCTS.forEach(p => {
        txs.push({
            id: id++,
            productId: p.id,
            productCode: p.productCode,
            productName: p.name,
            type: 'IN',
            quantity: p.currentStock + 20,
            reason: 'Initial Warehouse Inbound Intake',
            performedBy: 'admin',
            transactionDate: new Date(now - (25 * dayMs)).toISOString()
        });
    });

    const outs = [
        { pid: 1, qty: 5, daysAgo: 18, reason: 'Sales Order #SO-8821' },
        { pid: 2, qty: 12, daysAgo: 16, reason: 'Corporate Bulk Order' },
        { pid: 3, qty: 18, daysAgo: 15, reason: 'Retail Store Dispatch' },
        { pid: 6, qty: 25, daysAgo: 14, reason: 'Direct Customer Order' },
        { pid: 7, qty: 20, daysAgo: 12, reason: 'Enterprise Cabling Project' },
        { pid: 10, qty: 15, daysAgo: 11, reason: 'Workstation Upgrades' },
        { pid: 14, qty: 18, daysAgo: 10, reason: 'Retail Order #SO-8910' },
        { pid: 16, qty: 14, daysAgo: 9, reason: 'Service Lab Maintenance' },
        { pid: 2, qty: 11, daysAgo: 7, reason: 'Sales Order #SO-8940' },
        { pid: 3, qty: 15, daysAgo: 6, reason: 'Bulk Sales Dispatch' },
        { pid: 5, qty: 14, daysAgo: 5, reason: 'Customer Orders #SO-8992' },
        { pid: 6, qty: 20, daysAgo: 4, reason: 'Sales Order #SO-9012' },
        { pid: 7, qty: 12, daysAgo: 3, reason: 'Network Deployment' },
        { pid: 10, qty: 10, daysAgo: 2, reason: 'Lab Maintenance' },
        { pid: 2, qty: 10, daysAgo: 1, reason: 'Retail Order #SO-9102' },
        { pid: 3, qty: 14, daysAgo: 1, reason: 'Customer Sales' },
        { pid: 5, qty: 16, daysAgo: 0.5, reason: 'Corporate Procurement' },
        { pid: 6, qty: 15, daysAgo: 0.25, reason: 'Retail Order #SO-9215' }
    ];

    outs.forEach(o => {
        const prod = DEFAULT_PRODUCTS.find(p => p.id === o.pid);
        if (prod) {
            txs.push({
                id: id++,
                productId: prod.id,
                productCode: prod.productCode,
                productName: prod.name,
                type: 'OUT',
                quantity: o.qty,
                reason: o.reason,
                performedBy: 'admin',
                transactionDate: new Date(now - (o.daysAgo * dayMs)).toISOString()
            });
        }
    });

    return txs.reverse();
}

const DEFAULT_PURCHASE_ORDERS = [
    { id: 1, poNumber: 'PO-2026-001', supplierId: 2, supplierName: 'TechWorld Components', productId: 6, productName: 'High-Speed HDMI 2.1 Cable (2m)', quantity: 60, unitPrice: 499.00, totalAmount: 29940.00, orderDate: new Date(Date.now() - 172800000).toISOString(), expectedDelivery: new Date(Date.now() + 172800000).toISOString().split('T')[0], status: 'Pending' },
    { id: 2, poNumber: 'PO-2026-002', supplierId: 2, supplierName: 'TechWorld Components', productId: 3, productName: 'Ergonomic Optical Mouse', quantity: 50, unitPrice: 1250.00, totalAmount: 62500.00, orderDate: new Date(Date.now() - 86400000).toISOString(), expectedDelivery: new Date(Date.now() + 259200000).toISOString().split('T')[0], status: 'Approved' },
    { id: 3, poNumber: 'PO-2026-003', supplierId: 1, supplierName: 'ABC Electronics Ltd', productId: 2, productName: 'Wireless Mechanical Keyboard', quantity: 40, unitPrice: 3450.00, totalAmount: 138000.00, orderDate: new Date(Date.now() - 86400000).toISOString(), expectedDelivery: new Date(Date.now() + 172800000).toISOString().split('T')[0], status: 'In Transit' }
];

// =============================================================================
// 2. PERSISTENT LOCAL DATABASE ENGINE (Browser Storage Fallback)
// =============================================================================
class LocalDatabase {
    constructor() {
        this.load();
    }

    load() {
        try {
            const p = localStorage.getItem('simr_db_products');
            const s = localStorage.getItem('simr_db_suppliers');
            const t = localStorage.getItem('simr_db_transactions');
            const po = localStorage.getItem('simr_db_po');

            this.products = p ? JSON.parse(p) : JSON.parse(JSON.stringify(DEFAULT_PRODUCTS));
            this.suppliers = s ? JSON.parse(s) : JSON.parse(JSON.stringify(DEFAULT_SUPPLIERS));
            this.transactions = t ? JSON.parse(t) : generateSeedTransactions();
            this.purchaseOrders = po ? JSON.parse(po) : JSON.parse(JSON.stringify(DEFAULT_PURCHASE_ORDERS));
        } catch (e) {
            this.products = JSON.parse(JSON.stringify(DEFAULT_PRODUCTS));
            this.suppliers = JSON.parse(JSON.stringify(DEFAULT_SUPPLIERS));
            this.transactions = generateSeedTransactions();
            this.purchaseOrders = JSON.parse(JSON.stringify(DEFAULT_PURCHASE_ORDERS));
        }
    }

    save() {
        try {
            localStorage.setItem('simr_db_products', JSON.stringify(this.products));
            localStorage.setItem('simr_db_suppliers', JSON.stringify(this.suppliers));
            localStorage.setItem('simr_db_transactions', JSON.stringify(this.transactions));
            localStorage.setItem('simr_db_po', JSON.stringify(this.purchaseOrders));
        } catch (e) {}
    }

    getProductStatus(p) {
        if (p.currentStock <= 0) return 'Out of Stock';
        if (p.currentStock <= Math.floor(p.minimumStock / 2)) return 'Critical';
        if (p.currentStock <= p.minimumStock) return 'Low Stock';
        return 'Normal';
    }

    getStats() {
        let low = 0, out = 0, totalVal = 0;
        this.products.forEach(p => {
            if (p.currentStock <= 0) out++;
            else if (p.currentStock <= p.minimumStock) low++;
            totalVal += (Number(p.unitPrice || 0) * p.currentStock);
        });
        return {
            totalProducts: this.products.length,
            lowStock: low,
            outOfStock: out,
            totalSuppliers: this.suppliers.length,
            inventoryValue: totalVal
        };
    }

    getDailyMovements(days = 14) {
        const dateMap = {};
        const now = new Date();

        for (let i = days - 1; i >= 0; i--) {
            const d = new Date(now.getTime() - i * 86400000);
            const dateStr = d.toISOString().split('T')[0];
            dateMap[dateStr] = { date: dateStr, stockIn: 0, stockOut: 0 };
        }

        this.transactions.forEach(t => {
            if (!t.transactionDate) return;
            const dateStr = t.transactionDate.split('T')[0];
            if (dateMap[dateStr]) {
                if (t.type === 'IN') dateMap[dateStr].stockIn += Number(t.quantity || 0);
                else if (t.type === 'OUT') dateMap[dateStr].stockOut += Number(t.quantity || 0);
            }
        });

        return Object.values(dateMap);
    }

    getReorderRecommendations(demandMultiplier = 1.0, leadTimeDelay = 0, safetyBufferFactor = 1.0) {
        const recs = [];
        const now = Date.now();
        const thirtyDaysAgo = now - 30 * 86400000;

        this.products.forEach(p => {
            const sup = this.suppliers.find(s => s.id === p.supplierId) || { leadTime: 3, name: 'Unassigned Vendor' };
            const baseLeadTime = sup.leadTime || 3;
            const simulatedLeadTime = Math.max(1, baseLeadTime + leadTimeDelay);

            let totalOut = 0;
            this.transactions.forEach(t => {
                if (t.productId === p.id && t.type === 'OUT') {
                    const txTime = new Date(t.transactionDate).getTime();
                    if (txTime >= thirtyDaysAgo) totalOut += Number(t.quantity || 0);
                }
            });

            let baseDailyDemand = totalOut > 0 ? (totalOut / 30.0) : Math.max(1.0, p.minimumStock / 10.0);
            let simulatedDailyDemand = Math.round(baseDailyDemand * demandMultiplier * 10) / 10;

            const simulatedSafetyStock = Math.max(2, Math.ceil(p.minimumStock * 0.4 * safetyBufferFactor));
            let simulatedROP = Math.ceil((simulatedDailyDemand * simulatedLeadTime) + simulatedSafetyStock);
            simulatedROP = Math.max(simulatedROP, p.minimumStock);

            const daysUntilDepletion = simulatedDailyDemand > 0 ? Math.round((p.currentStock / simulatedDailyDemand) * 10) / 10 : 999;

            let urgency = 'Sufficient';
            let reorderRequired = false;
            let suggestedQty = 0;
            let riskScore = 15;

            if (p.currentStock <= 0) {
                urgency = 'Critical';
                reorderRequired = true;
                suggestedQty = Math.max(p.reorderQuantity || 30, (p.maximumStock || 100) - p.currentStock + (simulatedDailyDemand * simulatedLeadTime));
                riskScore = 100;
            } else if (daysUntilDepletion <= simulatedLeadTime) {
                urgency = 'Critical';
                reorderRequired = true;
                suggestedQty = Math.max(p.reorderQuantity || 30, (p.maximumStock || 100) - p.currentStock + (simulatedDailyDemand * simulatedLeadTime));
                riskScore = Math.min(99, Math.round(85 + 15 * (1.0 - (daysUntilDepletion / simulatedLeadTime))));
            } else if (p.currentStock <= simulatedROP) {
                urgency = 'Low Stock';
                reorderRequired = true;
                suggestedQty = Math.max(p.reorderQuantity || 25, (p.maximumStock || 100) - p.currentStock);
                riskScore = Math.min(80, Math.round(50 + 30 * (1.0 - (daysUntilDepletion / (simulatedLeadTime * 2)))));
            }

            recs.push({
                productId: p.id,
                productCode: p.productCode,
                productName: p.name,
                category: p.category,
                currentStock: p.currentStock,
                reorderLevel: p.minimumStock,
                dailyDemand: simulatedDailyDemand,
                leadTime: simulatedLeadTime,
                safetyStock: simulatedSafetyStock,
                reorderPoint: simulatedROP,
                daysUntilDepletion: daysUntilDepletion,
                suggestedQuantity: suggestedQty,
                supplierId: p.supplierId || 0,
                supplierName: p.supplierName || sup.name,
                unitPrice: p.unitPrice,
                estimatedCost: reorderRequired ? (Number(p.unitPrice || 0) * suggestedQty) : 0,
                urgencyStatus: urgency,
                reorderRequired: reorderRequired,
                riskScore: riskScore
            });
        });

        recs.sort((a, b) => b.riskScore - a.riskScore);
        return recs;
    }
}

const db = new LocalDatabase();

// Global Application State
const state = {
    currentUser: { username: 'admin', fullName: 'System Administrator', role: 'Administrator' },
    currentView: 'dashboard',
    products: [],
    suppliers: [],
    reorderRecommendations: [],
    chartInstance: null,
    activeReport: 'inventory',
    activeZone: 'ALL',
    deleteTarget: { type: null, id: null, name: '' },
    audioEnabled: true,
    theme: 'cyber-dark',
    javaBackendOnline: false,
    simulationParams: {
        demandMultiplier: 1.0,
        leadTimeDelay: 0,
        safetyBufferFactor: 1.0
    }
};

// =============================================================================
// 3. WEB AUDIO SFX SYNTHESIZER (Pure Client-Side Native Audio)
// =============================================================================
let audioCtx = null;

function getAudioContext() {
    if (!audioCtx && (window.AudioContext || window.webkitAudioContext)) {
        const AudioClass = window.AudioContext || window.webkitAudioContext;
        audioCtx = new AudioClass();
    }
    if (audioCtx && audioCtx.state === 'suspended') {
        audioCtx.resume();
    }
    return audioCtx;
}

function playAudioFx(type = 'click') {
    if (!state.audioEnabled) return;
    try {
        const ctx = getAudioContext();
        if (!ctx) return;
        const now = ctx.currentTime;

        if (type === 'click') {
            const osc = ctx.createOscillator();
            const gain = ctx.createGain();
            osc.type = 'sine';
            osc.frequency.setValueAtTime(800, now);
            osc.frequency.exponentialRampToValueAtTime(1400, now + 0.04);
            gain.gain.setValueAtTime(0.08, now);
            gain.gain.exponentialRampToValueAtTime(0.001, now + 0.04);
            osc.connect(gain);
            gain.connect(ctx.destination);
            osc.start(now);
            osc.stop(now + 0.04);
        } else if (type === 'laser') {
            const osc = ctx.createOscillator();
            const gain = ctx.createGain();
            osc.type = 'sawtooth';
            osc.frequency.setValueAtTime(2600, now);
            osc.frequency.exponentialRampToValueAtTime(400, now + 0.15);
            gain.gain.setValueAtTime(0.12, now);
            gain.gain.exponentialRampToValueAtTime(0.001, now + 0.15);
            osc.connect(gain);
            gain.connect(ctx.destination);
            osc.start(now);
            osc.stop(now + 0.15);
        } else if (type === 'success') {
            const osc1 = ctx.createOscillator();
            const osc2 = ctx.createOscillator();
            const gain = ctx.createGain();
            osc1.type = 'triangle';
            osc2.type = 'sine';
            osc1.frequency.setValueAtTime(523.25, now); // C5
            osc2.frequency.setValueAtTime(659.25, now + 0.06); // E5
            gain.gain.setValueAtTime(0.1, now);
            gain.gain.exponentialRampToValueAtTime(0.001, now + 0.28);
            osc1.connect(gain);
            osc2.connect(gain);
            gain.connect(ctx.destination);
            osc1.start(now);
            osc2.start(now + 0.06);
            osc1.stop(now + 0.28);
            osc2.stop(now + 0.28);
        } else if (type === 'alert') {
            const osc = ctx.createOscillator();
            const gain = ctx.createGain();
            osc.type = 'square';
            osc.frequency.setValueAtTime(320, now);
            osc.frequency.setValueAtTime(420, now + 0.08);
            gain.gain.setValueAtTime(0.12, now);
            gain.gain.exponentialRampToValueAtTime(0.001, now + 0.2);
            osc.connect(gain);
            gain.connect(ctx.destination);
            osc.start(now);
            osc.stop(now + 0.2);
        }
    } catch (e) {}
}

// =============================================================================
// 4. SVG BARCODE & QR CODE GENERATOR (Pure JavaScript, Zero Dependencies)
// =============================================================================
function generateBarcodeSvg(code = 'PRD-1001') {
    let hash = 0;
    for (let i = 0; i < code.length; i++) {
        hash = (hash << 5) - hash + code.charCodeAt(i);
        hash |= 0;
    }
    const seed = Math.abs(hash);

    let bars = '';
    let x = 10;
    const barHeight = 45;

    // Fixed start pattern
    bars += `<rect x="${x}" y="5" width="2" height="${barHeight}" fill="currentColor" />`; x += 4;
    bars += `<rect x="${x}" y="5" width="3" height="${barHeight}" fill="currentColor" />`; x += 5;

    // Pattern generator based on string characters
    for (let i = 0; i < code.length; i++) {
        const charVal = code.charCodeAt(i);
        const w1 = ((charVal + i) % 3) + 1;
        const w2 = (((charVal * 2) + seed + i) % 3) + 1;
        bars += `<rect x="${x}" y="5" width="${w1}" height="${barHeight}" fill="currentColor" />`;
        x += w1 + 2;
        bars += `<rect x="${x}" y="5" width="${w2}" height="${barHeight}" fill="currentColor" />`;
        x += w2 + 2;
    }

    // Fixed stop pattern
    bars += `<rect x="${x}" y="5" width="3" height="${barHeight}" fill="currentColor" />`; x += 5;
    bars += `<rect x="${x}" y="5" width="2" height="${barHeight}" fill="currentColor" />`; x += 10;

    return `
      <svg viewBox="0 0 ${x} 68" width="100%" height="68" xmlns="http://www.w3.org/2000/svg" style="color: var(--text-main);">
        ${bars}
        <text x="${x / 2}" y="62" font-family="'JetBrains Mono', monospace" font-size="11" text-anchor="middle" fill="currentColor">${escapeHtml(code)}</text>
      </svg>
    `;
}

function generateQrCodeSvg(text = 'SIMR-QR') {
    let hash = 0;
    for (let i = 0; i < text.length; i++) {
        hash = (hash << 5) - hash + text.charCodeAt(i);
        hash |= 0;
    }
    const size = 17;
    const cell = 6;
    const dim = size * cell;
    let rects = '';

    // Corners finder patterns
    function addFinder(startX, startY) {
        rects += `<rect x="${startX * cell}" y="${startY * cell}" width="${7 * cell}" height="${7 * cell}" fill="currentColor"/>`;
        rects += `<rect x="${(startX + 1) * cell}" y="${(startY + 1) * cell}" width="${5 * cell}" height="${5 * cell}" fill="var(--bg-card-solid)"/>`;
        rects += `<rect x="${(startX + 2) * cell}" y="${(startY + 2) * cell}" width="${3 * cell}" height="${3 * cell}" fill="currentColor"/>`;
    }

    addFinder(0, 0);
    addFinder(size - 7, 0);
    addFinder(0, size - 7);

    // Pseudorandom internal matrix
    for (let r = 0; r < size; r++) {
        for (let c = 0; c < size; c++) {
            if ((r < 7 && c < 7) || (r < 7 && c >= size - 7) || (r >= size - 7 && c < 7)) continue;
            const bit = ((hash ^ (r * 31 + c * 17)) & 1) === 1;
            if (bit) {
                rects += `<rect x="${c * cell}" y="${r * cell}" width="${cell - 0.5}" height="${cell - 0.5}" fill="currentColor"/>`;
            }
        }
    }

    return `
      <svg viewBox="0 0 ${dim} ${dim}" width="${dim}" height="${dim}" xmlns="http://www.w3.org/2000/svg" style="color: var(--cyan-neon);">
        ${rects}
      </svg>
    `;
}

// =============================================================================
// 5. APPLICATION INITIALIZATION & THEMES
// =============================================================================
document.addEventListener('DOMContentLoaded', () => {
    initThemeAndAudio();
    initAuth();
    initRouting();
    initModals();
    initEventListeners();
    checkBackendHealth();

    const urlParams = new URLSearchParams(window.location.search);
    const storedUser = localStorage.getItem('simr_user');
    if (storedUser || urlParams.get('demo') === 'true' || urlParams.get('autologin') === 'true') {
        try {
            state.currentUser = storedUser ? JSON.parse(storedUser) : {
                username: 'admin',
                fullName: 'System Administrator',
                role: 'Administrator'
            };
            showApp();
        } catch (e) {
            showLogin();
        }
    } else {
        showLogin();
    }
});

function initThemeAndAudio() {
    const savedTheme = localStorage.getItem('simr_theme') || 'cyber-dark';
    state.theme = savedTheme;
    document.body.className = savedTheme;
    updateThemeButtonUi();

    const savedAudio = localStorage.getItem('simr_audio_sfx');
    state.audioEnabled = (savedAudio === null || savedAudio === 'true');
    updateAudioButtonUi();

    document.getElementById('btnToggleTheme')?.addEventListener('click', () => {
        playAudioFx('click');
        state.theme = (state.theme === 'cyber-dark') ? 'cyber-light' : 'cyber-dark';
        document.body.className = state.theme;
        localStorage.setItem('simr_theme', state.theme);
        updateThemeButtonUi();
        if (state.chartInstance) state.chartInstance.render();
        showToast(`Theme switched: ${state.theme === 'cyber-dark' ? 'Cyber Matrix' : 'Next-Gen Frost'}`, 'info');
    });

    document.getElementById('btnToggleAudio')?.addEventListener('click', () => {
        state.audioEnabled = !state.audioEnabled;
        localStorage.setItem('simr_audio_sfx', state.audioEnabled);
        updateAudioButtonUi();
        if (state.audioEnabled) playAudioFx('success');
        showToast(`Sound FX ${state.audioEnabled ? 'Activated' : 'Muted'}`, 'info');
    });
}

function updateThemeButtonUi() {
    const txt = document.getElementById('themeStatusText');
    if (txt) txt.textContent = (state.theme === 'cyber-dark') ? 'DARK' : 'LIGHT';
}

function updateAudioButtonUi() {
    const txt = document.getElementById('audioStatusText');
    if (txt) txt.textContent = state.audioEnabled ? 'SFX: ON' : 'SFX: OFF';
}

async function checkBackendHealth() {
    try {
        const resp = await fetch('/api/dashboard/stats', { method: 'GET' });
        if (resp.ok) {
            const data = await resp.json();
            if (data.success) {
                state.javaBackendOnline = true;
                const pill = document.getElementById('systemDbPill');
                if (pill) pill.textContent = 'CORE: ONLINE (JAVA 21)';
                return;
            }
        }
    } catch (e) {}
    state.javaBackendOnline = false;
    const pill = document.getElementById('systemDbPill');
    if (pill) pill.textContent = 'CORE: ACTIVE (STANDALONE)';
}

// =============================================================================
// 6. AUTHENTICATION
// =============================================================================
function initAuth() {
    const loginForm = document.getElementById('loginForm');
    const btnFillDemo = document.getElementById('btnFillDemo');
    const btnTogglePassword = document.getElementById('btnTogglePassword');
    const loginPassword = document.getElementById('loginPassword');
    const btnLogout = document.getElementById('btnLogout');

    btnFillDemo?.addEventListener('click', () => {
        playAudioFx('click');
        document.getElementById('loginUsername').value = 'admin';
        document.getElementById('loginPassword').value = 'admin123';
        showToast('Demo credentials autofilled: admin / admin123', 'info');
    });

    btnTogglePassword?.addEventListener('click', () => {
        playAudioFx('click');
        const isPassword = loginPassword.type === 'password';
        loginPassword.type = isPassword ? 'text' : 'password';
    });

    loginForm?.addEventListener('submit', (e) => {
        e.preventDefault();
        const username = document.getElementById('loginUsername').value.trim();
        const password = document.getElementById('loginPassword').value.trim();

        if ((username === 'admin' && password === 'admin123') || (username.length > 0 && password.length > 0)) {
            playAudioFx('success');
            state.currentUser = {
                username: username,
                fullName: username === 'admin' ? 'System Administrator' : username,
                role: 'Administrator'
            };
            if (document.getElementById('rememberMe').checked) {
                localStorage.setItem('simr_user', JSON.stringify(state.currentUser));
            }
            showToast(`Identity verified. Welcome back, ${state.currentUser.fullName}!`, 'success');
            showApp();
        } else {
            playAudioFx('alert');
            showToast('Authentication failed. Use admin / admin123', 'error');
        }
    });

    btnLogout?.addEventListener('click', () => {
        playAudioFx('click');
        state.currentUser = null;
        localStorage.removeItem('simr_user');
        showToast('Session terminated.', 'info');
        showLogin();
    });
}

function showLogin() {
    document.getElementById('loginView').style.display = 'flex';
    document.getElementById('appContainer').style.display = 'none';
}

function showApp() {
    document.getElementById('loginView').style.display = 'none';
    document.getElementById('appContainer').style.display = 'flex';

    if (state.currentUser) {
        document.getElementById('sidebarUserName').textContent = state.currentUser.fullName || state.currentUser.username;
        document.getElementById('sidebarAvatar').textContent = (state.currentUser.username || 'A').charAt(0).toUpperCase();
    }

    if (!state.chartInstance) {
        state.chartInstance = new StockMovementChart('stockMovementCanvas');
    }

    loadAllData();
    navigate(window.location.hash.replace('#', '') || 'dashboard');
}

// =============================================================================
// 7. ROUTING & NAVIGATION
// =============================================================================
function initRouting() {
    window.addEventListener('hashchange', () => {
        navigate(window.location.hash.replace('#', '') || 'dashboard');
    });

    document.querySelectorAll('.sidebar-nav .nav-item').forEach(item => {
        item.addEventListener('click', () => {
            playAudioFx('click');
            const view = item.getAttribute('data-view');
            if (view) {
                navigate(view);
                document.getElementById('appSidebar').classList.remove('open');
            }
        });
    });

    document.getElementById('btnSidebarToggle')?.addEventListener('click', () => {
        playAudioFx('click');
        document.getElementById('appSidebar').classList.toggle('open');
    });

    // Notification dropdown
    const btnNotif = document.getElementById('btnNotificationBell');
    const notifDropdown = document.getElementById('notificationDropdown');
    btnNotif?.addEventListener('click', (e) => {
        e.stopPropagation();
        playAudioFx('click');
        notifDropdown.classList.toggle('show');
    });
    document.addEventListener('click', () => {
        notifDropdown?.classList.remove('show');
    });
}

function navigate(viewName) {
    const validViews = ['dashboard', 'inventory', 'suppliers', 'stock', 'warehouse', 'reorder', 'purchase-orders', 'reports'];
    if (!validViews.includes(viewName)) viewName = 'dashboard';

    state.currentView = viewName;

    document.querySelectorAll('.sidebar-nav .nav-item').forEach(item => {
        item.classList.toggle('active', item.getAttribute('data-view') === viewName);
    });

    document.querySelectorAll('.view-section').forEach(sec => {
        sec.classList.remove('active');
    });

    const targetId = 'view' + viewName.split('-').map(capitalize).join('');
    const target = document.getElementById(targetId);
    if (target) target.classList.add('active');

    const titles = {
        dashboard: { title: 'Dashboard HUD', sub: 'Real-time inventory telemetry and predictive reorder stream' },
        inventory: { title: 'Inventory Catalog & SKU Matrix', sub: 'Monitor product catalog, unit prices, thresholds, and barcodes' },
        suppliers: { title: 'Supplier Network & Reliability', sub: 'Manage verified vendors, fulfillment lead times, and reliability scoring' },
        stock: { title: 'Stock Operations // In & Out', sub: 'Record replenishment intake and customer dispatches with stock protection' },
        warehouse: { title: 'Warehouse Digital Twin // 2D Spatial Grid', sub: 'Interactive aisle rack bays with real-time capacity heatmaps' },
        reorder: { title: 'Predictive AI Reorder & Simulation Sandbox', sub: 'Model market demand surges and supply chain shocks in real-time' },
        'purchase-orders': { title: 'Purchase Orders & Supplier Invoices', sub: 'Track procurement orders with printable invoices and QR verification' },
        reports: { title: 'Intelligence Reports & Auditing', sub: 'Generate detailed valuation reports, CSV datasets, and database backups' }
    };

    if (titles[viewName]) {
        document.getElementById('topbarTitle').textContent = titles[viewName].title;
        document.getElementById('topbarSubtitle').textContent = titles[viewName].sub;
    }

    refreshView(viewName);
}

function loadAllData() {
    state.products = db.products;
    state.suppliers = db.suppliers;
    loadDashboardView();
    loadInventoryView();
    loadSuppliersView();
    loadStockView();
    loadWarehouseView();
    loadReorderView();
    loadPurchaseOrdersView();
    populateProductDropdowns(state.products);
    populateSupplierDropdowns(state.suppliers);
}

function refreshView(viewName) {
    switch (viewName) {
        case 'dashboard': loadDashboardView(); break;
        case 'inventory': loadInventoryView(); break;
        case 'suppliers': loadSuppliersView(); break;
        case 'stock': loadStockView(); break;
        case 'warehouse': loadWarehouseView(); break;
        case 'reorder': loadReorderView(); break;
        case 'purchase-orders': loadPurchaseOrdersView(); break;
        case 'reports': loadReportView(state.activeReport); break;
    }
}

// =============================================================================
// 8. VIEW 1: DASHBOARD HUD
// =============================================================================
function loadDashboardView() {
    const stats = db.getStats();
    document.getElementById('statTotalProducts').textContent = stats.totalProducts;
    document.getElementById('statLowStock').textContent = stats.lowStock;
    document.getElementById('statOutOfStock').textContent = stats.outOfStock;
    document.getElementById('statTotalSuppliers').textContent = stats.totalSuppliers;
    document.getElementById('statInventoryValue').textContent = formatCurrency(stats.inventoryValue);

    const urgentCount = stats.lowStock + stats.outOfStock;
    const notifBadge = document.getElementById('notificationBadge');
    const sidebarBadge = document.getElementById('sidebarReorderBadge');
    const notifCountBadge = document.getElementById('notifCountBadge');

    if (urgentCount > 0) {
        notifBadge.style.display = 'block';
        sidebarBadge.style.display = 'inline-block';
        sidebarBadge.textContent = urgentCount;
        notifCountBadge.textContent = urgentCount + ' alerts';
    } else {
        notifBadge.style.display = 'none';
        sidebarBadge.style.display = 'none';
        notifCountBadge.textContent = '0 alerts';
    }

    if (state.chartInstance) {
        state.chartInstance.setData(db.getDailyMovements(14));
    }

    const alerts = db.products.filter(p => p.currentStock <= p.minimumStock).sort((a, b) => a.currentStock - b.currentStock);
    renderDashboardAlerts(alerts);
    renderDashboardActivity(db.transactions.slice(0, 6));
}

function renderDashboardAlerts(alerts) {
    const tbody = document.getElementById('dashboardAlertsTableBody');
    const notifList = document.getElementById('notificationList');

    if (!alerts || alerts.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" style="padding: 24px; text-align: center; color: var(--emerald-neon);">All inventory levels are optimal. Zero critical shortages.</td></tr>`;
        if (notifList) notifList.innerHTML = '<div style="padding: 12px; text-align: center; color: var(--text-muted);">No critical alerts</div>';
        return;
    }

    let rows = '';
    let notifs = '';
    alerts.slice(0, 5).forEach(p => {
        const st = db.getProductStatus(p);
        const badgeClass = st === 'Out of Stock' ? 'badge-out' : (st === 'Critical' ? 'badge-critical' : 'badge-low');
        rows += `
            <tr>
                <td>
                    <div style="font-weight: 600;">${escapeHtml(p.name)}</div>
                    <span class="badge-code">${escapeHtml(p.productCode)}</span>
                </td>
                <td style="font-weight: 700; font-family: var(--font-mono);">${p.currentStock}</td>
                <td style="font-family: var(--font-mono);">${p.minimumStock}</td>
                <td><span class="badge ${badgeClass}">${escapeHtml(st)}</span></td>
            </tr>
        `;
        notifs += `
            <div class="notification-item" onclick="navigate('reorder')">
                <div style="display: flex; justify-content: space-between;">
                    <strong>${escapeHtml(p.name)}</strong>
                    <span class="badge ${badgeClass}">${escapeHtml(st)}</span>
                </div>
                <div style="color: var(--text-muted); font-size: 0.75rem;">Current: ${p.currentStock} | Min: ${p.minimumStock}</div>
            </div>
        `;
    });

    tbody.innerHTML = rows;
    if (notifList) notifList.innerHTML = notifs;
}

function renderDashboardActivity(transactions) {
    const tbody = document.getElementById('dashboardActivityTableBody');
    if (!transactions || transactions.length === 0) {
        tbody.innerHTML = `<tr><td colspan="3" style="text-align: center; padding: 20px;">No transaction records found.</td></tr>`;
        return;
    }

    let rows = '';
    transactions.forEach(t => {
        const isIn = t.type === 'IN';
        const badgeClass = isIn ? 'badge-normal' : 'badge-critical';
        const sign = isIn ? '+' : '-';
        const dateStr = t.transactionDate ? new Date(t.transactionDate).toLocaleString('en-IN', { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' }) : '--';

        rows += `
            <tr>
                <td>
                    <div style="font-weight: 600;">${escapeHtml(t.productName || 'Product #' + t.productId)}</div>
                    <div style="font-size: 0.75rem; color: var(--text-muted);">${escapeHtml(t.reason || 'Inventory Transaction')}</div>
                </td>
                <td>
                    <span class="badge ${badgeClass}" style="font-family: var(--font-mono);">${sign}${t.quantity}</span>
                </td>
                <td style="font-size: 0.8125rem; font-family: var(--font-mono); color: var(--text-muted);">${dateStr}</td>
            </tr>
        `;
    });
    tbody.innerHTML = rows;
}

// =============================================================================
// 9. VIEW 2: INVENTORY MANAGEMENT
// =============================================================================
function loadInventoryView() {
    const query = (document.getElementById('inventorySearchInput')?.value || '').toLowerCase().trim();
    const cat = document.getElementById('inventoryCategoryFilter')?.value || 'ALL';
    const status = document.getElementById('inventoryStatusFilter')?.value || 'ALL';

    const filtered = db.products.filter(p => {
        const matchQuery = !query || p.name.toLowerCase().includes(query) || p.productCode.toLowerCase().includes(query);
        const matchCat = cat === 'ALL' || p.category === cat;
        const pStatus = db.getProductStatus(p);
        const matchStatus = status === 'ALL' || pStatus === status;
        return matchQuery && matchCat && matchStatus;
    });

    document.getElementById('inventoryCountBadge').textContent = `Showing ${filtered.length} items`;
    renderInventoryTable(filtered);
}

function renderInventoryTable(products) {
    const tbody = document.getElementById('inventoryTableBody');
    if (!products || products.length === 0) {
        tbody.innerHTML = `<tr><td colspan="9" style="text-align: center; padding: 30px; color: var(--text-muted);">No products match your filter parameters.</td></tr>`;
        return;
    }

    let rows = '';
    products.forEach(p => {
        const st = db.getProductStatus(p);
        const badgeClass = st === 'Out of Stock' ? 'badge-out' : (st === 'Critical' ? 'badge-critical' : (st === 'Low Stock' ? 'badge-low' : 'badge-normal'));

        rows += `
            <tr>
                <td>
                    <span class="badge-code">${escapeHtml(p.productCode)}</span>
                    <div style="font-size: 0.6875rem; color: var(--text-light); margin-top: 2px;">Rack: ${escapeHtml(p.rackBay || 'Unassigned')}</div>
                </td>
                <td>
                    <div style="font-weight: 600;">${escapeHtml(p.name)}</div>
                </td>
                <td><span style="font-size: 0.8125rem;">${escapeHtml(p.category || 'General')}</span></td>
                <td><span style="font-size: 0.8125rem; color: var(--text-muted);">${escapeHtml(p.supplierName || 'None')}</span></td>
                <td style="font-family: var(--font-mono); font-weight: 600;">${formatCurrency(p.unitPrice)}</td>
                <td style="font-family: var(--font-mono); font-weight: 700;">${p.currentStock}</td>
                <td style="font-family: var(--font-mono);">${p.minimumStock}</td>
                <td><span class="badge ${badgeClass}">${escapeHtml(st)}</span></td>
                <td>
                    <div class="action-btn-group">
                        <button type="button" class="table-action-btn" title="View Telemetry & Barcode" onclick="openViewProductModal(${p.id})">
                            <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                        <button type="button" class="table-action-btn" title="Edit Product" onclick="openEditProductModal(${p.id})">
                            <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                            </svg>
                        </button>
                        <button type="button" class="table-action-btn danger" title="Delete Product" onclick="confirmDeleteProduct(${p.id}, '${escapeJs(p.name)}')">
                            <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2">
                                <polyline points="3 6 5 6 21 6"></polyline>
                                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                            </svg>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    });

    tbody.innerHTML = rows;
}

// =============================================================================
// 10. VIEW 3: SUPPLIER MATRIX
// =============================================================================
function loadSuppliersView() {
    const tbody = document.getElementById('suppliersTableBody');
    if (!db.suppliers || db.suppliers.length === 0) {
        tbody.innerHTML = `<tr><td colspan="10" style="text-align: center; padding: 24px;">No suppliers registered.</td></tr>`;
        return;
    }

    let rows = '';
    db.suppliers.forEach(s => {
        const prodCount = db.products.filter(p => p.supplierId === s.id).length;
        const rel = s.reliabilityScore || 94;
        const relColor = rel >= 90 ? 'var(--emerald-neon)' : (rel >= 75 ? 'var(--amber-neon)' : 'var(--rose-neon)');

        rows += `
            <tr>
                <td><span class="badge-code">${escapeHtml(s.supplierCode)}</span></td>
                <td><div style="font-weight: 600;">${escapeHtml(s.name)}</div></td>
                <td>${escapeHtml(s.contactPerson)}</td>
                <td style="font-family: var(--font-mono);">${escapeHtml(s.phone)}</td>
                <td><a href="mailto:${escapeHtml(s.email)}" style="font-size: 0.8125rem;">${escapeHtml(s.email)}</a></td>
                <td style="font-family: var(--font-mono);">${s.leadTime} Days</td>
                <td>
                    <div style="display: flex; align-items: center; gap: 6px;">
                        <span style="font-weight: 700; color: ${relColor}; font-family: var(--font-mono);">${rel}%</span>
                    </div>
                </td>
                <td style="font-family: var(--font-mono);">${prodCount}</td>
                <td><span class="badge ${s.status === 'Active' ? 'badge-normal' : 'badge-critical'}">${s.status}</span></td>
                <td>
                    <div class="action-btn-group">
                        <button type="button" class="table-action-btn" title="Edit Supplier" onclick="openEditSupplierModal(${s.id})">
                            <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                            </svg>
                        </button>
                        <button type="button" class="table-action-btn danger" title="Delete Supplier" onclick="confirmDeleteSupplier(${s.id}, '${escapeJs(s.name)}')">
                            <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2">
                                <polyline points="3 6 5 6 21 6"></polyline>
                                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                            </svg>
                        </button>
                    </div>
                </td>
            </tr>
        `;
    });

    tbody.innerHTML = rows;
}

// =============================================================================
// 11. VIEW 4: STOCK OPERATIONS
// =============================================================================
function loadStockView() {
    renderStockTransactionsTable(db.transactions);
}

function renderStockTransactionsTable(transactions) {
    const tbody = document.getElementById('stockTransactionsTableBody');
    if (!transactions || transactions.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; padding: 24px;">No stock movements recorded yet.</td></tr>`;
        return;
    }

    let rows = '';
    transactions.slice(0, 20).forEach(t => {
        const isIn = t.type === 'IN';
        const badgeClass = isIn ? 'badge-normal' : 'badge-critical';
        const sign = isIn ? '+' : '-';
        const dateStr = t.transactionDate ? new Date(t.transactionDate).toLocaleString('en-IN', { dateStyle: 'medium', timeStyle: 'short' }) : '--';

        rows += `
            <tr>
                <td><span style="font-family: var(--font-mono); color: var(--text-light);">#${t.id}</span></td>
                <td><span class="badge-code">${escapeHtml(t.productCode || '--')}</span></td>
                <td style="font-weight: 600;">${escapeHtml(t.productName || 'Product #' + t.productId)}</td>
                <td><span class="badge ${badgeClass}">${isIn ? 'INTAKE' : 'DISPATCH'}</span></td>
                <td style="font-family: var(--font-mono); font-weight: 700;">${sign}${t.quantity}</td>
                <td><span style="color: var(--text-muted); font-size: 0.8125rem;">${escapeHtml(t.reason || 'Inventory Operation')}</span></td>
                <td style="font-family: var(--font-mono); font-size: 0.75rem;">${dateStr}</td>
                <td style="font-size: 0.75rem; color: var(--cyan-neon);">${escapeHtml(t.performedBy || 'admin')}</td>
            </tr>
        `;
    });

    tbody.innerHTML = rows;
}

// =============================================================================
// 12. VIEW 5: WAREHOUSE DIGITAL TWIN (2D SPATIAL RACK MATRIX)
// =============================================================================
function loadWarehouseView() {
    const container = document.getElementById('warehouseGridContainer');
    if (!container) return;

    // Filter products by selected zone
    const prods = state.activeZone === 'ALL' 
        ? db.products 
        : db.products.filter(p => p.category === state.activeZone);

    let html = '';
    prods.forEach(p => {
        const pct = Math.min(100, Math.round((p.currentStock / (p.maximumStock || 100)) * 100));
        let fillClass = 'optimal';
        if (p.currentStock <= 0) fillClass = 'critical';
        else if (p.currentStock <= p.minimumStock) fillClass = 'warning';

        html += `
            <div class="rack-bay-card" onclick="openViewProductModal(${p.id})">
                <div class="rack-bay-header">
                    <span class="rack-bay-code">BAY // ${escapeHtml(p.rackBay || 'A-1')}</span>
                    <span class="rack-bay-zone">${escapeHtml(p.category)}</span>
                </div>
                <div style="font-weight: 700; font-size: 0.9375rem; margin-bottom: 4px;">${escapeHtml(p.name)}</div>
                <div style="font-size: 0.75rem; color: var(--cyan-neon); font-family: var(--font-mono); margin-bottom: 12px;">SKU: ${escapeHtml(p.productCode)}</div>

                <div class="shelf-slot-list">
                    <div class="shelf-slot">
                        <span class="shelf-slot-name">Current Inventory</span>
                        <div style="display: flex; align-items: center; gap: 8px;">
                            <span style="font-family: var(--font-mono); font-weight: 700; font-size: 0.8125rem;">${p.currentStock} / ${p.maximumStock || 100}</span>
                            <div class="shelf-capacity-bar">
                                <div class="shelf-capacity-fill ${fillClass}" style="width: ${pct}%;"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 14px; font-size: 0.75rem; color: var(--text-muted);">
                    <span>Utilization: <strong style="color: var(--text-main); font-family: var(--font-mono);">${pct}%</strong></span>
                    <span style="color: var(--cyan-neon); text-decoration: underline;">Inspect Shelf &rarr;</span>
                </div>
            </div>
        `;
    });

    if (prods.length === 0) {
        html = `<div style="grid-column: 1/-1; text-align: center; padding: 40px; color: var(--text-muted);">No inventory racks assigned in this zone.</div>`;
    }

    container.innerHTML = html;
}

// =============================================================================
// 13. VIEW 6: PREDICTIVE AI REORDER & SIMULATION SANDBOX
// =============================================================================
function loadReorderView() {
    const { demandMultiplier, leadTimeDelay, safetyBufferFactor } = state.simulationParams;
    const recs = db.getReorderRecommendations(demandMultiplier, leadTimeDelay, safetyBufferFactor);
    state.reorderRecommendations = recs;

    const urgentCount = recs.filter(r => r.reorderRequired).length;
    const summaryBadge = document.getElementById('reorderSummaryBadge');
    if (summaryBadge) {
        summaryBadge.textContent = `${urgentCount} Items Require Reorder`;
        summaryBadge.className = urgentCount > 0 ? 'badge badge-critical' : 'badge badge-normal';
    }

    renderReorderTable(recs);
}

function renderReorderTable(recommendations) {
    const tbody = document.getElementById('reorderRecommendationsTableBody');
    if (!recommendations || recommendations.length === 0) {
        tbody.innerHTML = `<tr><td colspan="10" style="text-align: center; padding: 24px;">No products available for predictive reorder analysis.</td></tr>`;
        return;
    }

    let rows = '';
    recommendations.forEach(r => {
        const isCritical = r.urgencyStatus === 'Critical';
        const isLow = r.urgencyStatus === 'Low Stock';
        const badgeClass = isCritical ? 'badge-critical' : (isLow ? 'badge-low' : 'badge-normal');

        const depletionText = r.daysUntilDepletion > 90 ? '>90 Days' : `${r.daysUntilDepletion} Days`;
        const depletionColor = r.daysUntilDepletion <= r.leadTime ? 'var(--rose-neon)' : (r.daysUntilDepletion <= r.leadTime * 2 ? 'var(--amber-neon)' : 'var(--emerald-neon)');

        let actionBtn = `<span style="font-size: 0.75rem; color: var(--emerald-neon);">Optimal</span>`;
        if (r.reorderRequired) {
            actionBtn = `
                <button type="button" class="btn btn-sm btn-primary" onclick="openReorderModalFromRec(${r.productId})">
                    <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M12 5v14M5 12h14"></path>
                    </svg>
                    Reorder
                </button>
            `;
        }

        rows += `
            <tr>
                <td>
                    <div style="font-weight: 600;">${escapeHtml(r.productName)}</div>
                    <span class="badge-code">${escapeHtml(r.productCode)}</span>
                </td>
                <td style="font-family: var(--font-mono); font-weight: 700;">${r.currentStock}</td>
                <td style="font-family: var(--font-mono);">${r.dailyDemand}/d</td>
                <td style="font-family: var(--font-mono);">${r.leadTime}d</td>
                <td style="font-family: var(--font-mono); font-weight: 600; color: var(--cyan-neon);">${r.reorderPoint}</td>
                <td style="font-family: var(--font-mono); font-weight: 700; color: ${depletionColor};">${depletionText}</td>
                <td style="font-family: var(--font-mono);">${r.suggestedQuantity}</td>
                <td style="font-size: 0.8125rem;">${escapeHtml(r.supplierName)}</td>
                <td><span class="badge ${badgeClass}">${escapeHtml(r.urgencyStatus)}</span></td>
                <td style="text-align: right;">${actionBtn}</td>
            </tr>
        `;
    });

    tbody.innerHTML = rows;
}

// =============================================================================
// 14. VIEW 7: PURCHASE ORDERS & INVOICES
// =============================================================================
function loadPurchaseOrdersView() {
    const tbody = document.getElementById('purchaseOrdersTableBody');
    if (!db.purchaseOrders || db.purchaseOrders.length === 0) {
        tbody.innerHTML = `<tr><td colspan="9" style="text-align: center; padding: 24px;">No purchase orders generated yet.</td></tr>`;
        return;
    }

    let rows = '';
    db.purchaseOrders.forEach(po => {
        let badgeClass = 'badge-low';
        if (po.status === 'Approved') badgeClass = 'badge-normal';
        else if (po.status === 'In Transit') badgeClass = 'badge-code';
        else if (po.status === 'Received') badgeClass = 'badge-normal';

        rows += `
            <tr>
                <td><strong style="font-family: var(--font-mono); color: var(--cyan-neon);">${escapeHtml(po.poNumber)}</strong></td>
                <td>${escapeHtml(po.supplierName)}</td>
                <td><div style="font-weight: 600;">${escapeHtml(po.productName)}</div></td>
                <td style="font-family: var(--font-mono); font-weight: 700;">${po.quantity}</td>
                <td style="font-family: var(--font-mono);">${formatCurrency(po.unitPrice)}</td>
                <td style="font-family: var(--font-mono); font-weight: 700; color: var(--cyan-neon);">${formatCurrency(po.totalAmount)}</td>
                <td style="font-family: var(--font-mono);">${po.expectedDelivery || '--'}</td>
                <td><span class="badge ${badgeClass}">${po.status || 'Pending'}</span></td>
                <td style="text-align: right;">
                    <button type="button" class="btn btn-sm btn-secondary" onclick="openPOInvoiceModal(${po.id})">
                        <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                            <polyline points="14 2 14 8 20 8"></polyline>
                        </svg>
                        View Invoice
                    </button>
                </td>
            </tr>
        `;
    });

    tbody.innerHTML = rows;
}

// =============================================================================
// 15. VIEW 8: REPORTS & CSV EXPORT
// =============================================================================
function loadReportView(reportType = 'inventory') {
    state.activeReport = reportType;
    document.querySelectorAll('.report-tab-btn').forEach(btn => {
        btn.classList.toggle('active', btn.getAttribute('data-report') === reportType);
    });

    const thead = document.getElementById('reportTableHead');
    const tbody = document.getElementById('reportTableBody');
    const title = document.getElementById('reportDisplayTitle');
    const subtitle = document.getElementById('reportDisplaySubtitle');
    const countBadge = document.getElementById('reportRowCountBadge');

    let rows = '';

    if (reportType === 'inventory') {
        title.textContent = 'Inventory Asset Valuation Report';
        subtitle.textContent = 'Live stock capitalization and unit cost audit';
        thead.innerHTML = `
            <tr>
                <th>Code</th>
                <th>Name</th>
                <th>Category</th>
                <th>Supplier</th>
                <th>Unit Price</th>
                <th>Current Stock</th>
                <th>Total Valuation</th>
                <th>Status</th>
            </tr>
        `;
        let totalVal = 0;
        db.products.forEach(p => {
            const val = (p.unitPrice || 0) * p.currentStock;
            totalVal += val;
            rows += `
                <tr>
                    <td><span class="badge-code">${escapeHtml(p.productCode)}</span></td>
                    <td style="font-weight: 600;">${escapeHtml(p.name)}</td>
                    <td>${escapeHtml(p.category)}</td>
                    <td>${escapeHtml(p.supplierName || '--')}</td>
                    <td style="font-family: var(--font-mono);">${formatCurrency(p.unitPrice)}</td>
                    <td style="font-family: var(--font-mono); font-weight: 700;">${p.currentStock}</td>
                    <td style="font-family: var(--font-mono); font-weight: 700; color: var(--cyan-neon);">${formatCurrency(val)}</td>
                    <td><span class="badge ${p.currentStock <= 0 ? 'badge-out' : 'badge-normal'}">${db.getProductStatus(p)}</span></td>
                </tr>
            `;
        });
        countBadge.textContent = `${db.products.length} records | Total: ${formatCurrency(totalVal)}`;
    } else if (reportType === 'low-stock') {
        title.textContent = 'Low Stock & Depletion Alert Report';
        subtitle.textContent = 'SKUs operating at or beneath reorder thresholds';
        thead.innerHTML = `
            <tr>
                <th>Code</th>
                <th>Name</th>
                <th>Category</th>
                <th>Current Stock</th>
                <th>Reorder Level</th>
                <th>Shortage Qty</th>
                <th>Status</th>
            </tr>
        `;
        const lowItems = db.products.filter(p => p.currentStock <= p.minimumStock);
        lowItems.forEach(p => {
            const deficit = p.minimumStock - p.currentStock;
            rows += `
                <tr>
                    <td><span class="badge-code">${escapeHtml(p.productCode)}</span></td>
                    <td style="font-weight: 600;">${escapeHtml(p.name)}</td>
                    <td>${escapeHtml(p.category)}</td>
                    <td style="font-family: var(--font-mono); font-weight: 700; color: var(--rose-neon);">${p.currentStock}</td>
                    <td style="font-family: var(--font-mono);">${p.minimumStock}</td>
                    <td style="font-family: var(--font-mono); font-weight: 700;">${deficit > 0 ? deficit : 'Critical'}</td>
                    <td><span class="badge badge-critical">${db.getProductStatus(p)}</span></td>
                </tr>
            `;
        });
        countBadge.textContent = `${lowItems.length} alert records`;
    } else if (reportType === 'suppliers') {
        title.textContent = 'Supplier Network & Performance Directory';
        subtitle.textContent = 'Vendor fulfillment reliability and lead-time audit';
        thead.innerHTML = `
            <tr>
                <th>Code</th>
                <th>Supplier Name</th>
                <th>Contact</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Lead Time</th>
                <th>Reliability</th>
                <th>Status</th>
            </tr>
        `;
        db.suppliers.forEach(s => {
            rows += `
                <tr>
                    <td><span class="badge-code">${escapeHtml(s.supplierCode)}</span></td>
                    <td style="font-weight: 600;">${escapeHtml(s.name)}</td>
                    <td>${escapeHtml(s.contactPerson)}</td>
                    <td style="font-family: var(--font-mono);">${escapeHtml(s.phone)}</td>
                    <td>${escapeHtml(s.email)}</td>
                    <td style="font-family: var(--font-mono);">${s.leadTime} Days</td>
                    <td style="font-family: var(--font-mono); font-weight: 700; color: var(--emerald-neon);">${s.reliabilityScore || 95}%</td>
                    <td><span class="badge badge-normal">${s.status}</span></td>
                </tr>
            `;
        });
        countBadge.textContent = `${db.suppliers.length} active suppliers`;
    } else if (reportType === 'stock-movement') {
        title.textContent = 'Stock Movement Transaction Audit Log';
        subtitle.textContent = 'Chronological record of verified intake and sales dispatches';
        thead.innerHTML = `
            <tr>
                <th>Tx ID</th>
                <th>Date / Time</th>
                <th>Product Code</th>
                <th>Product Name</th>
                <th>Type</th>
                <th>Quantity</th>
                <th>Reason / Reference</th>
                <th>Agent</th>
            </tr>
        `;
        db.transactions.forEach(t => {
            const isIn = t.type === 'IN';
            rows += `
                <tr>
                    <td style="font-family: var(--font-mono);">#${t.id}</td>
                    <td style="font-family: var(--font-mono); font-size: 0.75rem;">${t.transactionDate ? new Date(t.transactionDate).toLocaleString('en-IN') : '--'}</td>
                    <td><span class="badge-code">${escapeHtml(t.productCode || '--')}</span></td>
                    <td style="font-weight: 600;">${escapeHtml(t.productName)}</td>
                    <td><span class="badge ${isIn ? 'badge-normal' : 'badge-critical'}">${isIn ? 'INTAKE' : 'DISPATCH'}</span></td>
                    <td style="font-family: var(--font-mono); font-weight: 700;">${isIn ? '+' : '-'}${t.quantity}</td>
                    <td>${escapeHtml(t.reason || '--')}</td>
                    <td style="color: var(--cyan-neon);">${escapeHtml(t.performedBy || 'admin')}</td>
                </tr>
            `;
        });
        countBadge.textContent = `${db.transactions.length} verified transactions`;
    }

    tbody.innerHTML = rows || `<tr><td colspan="8" style="text-align:center; padding: 24px;">No records available.</td></tr>`;
}

function exportActiveReportCsv() {
    playAudioFx('laser');
    let csv = '';
    const reportType = state.activeReport;

    if (reportType === 'inventory') {
        csv = 'Product Code,Product Name,Category,Supplier,Unit Price,Current Stock,Valuation,Status\n';
        db.products.forEach(p => {
            const val = (p.unitPrice || 0) * p.currentStock;
            csv += `"${p.productCode}","${p.name}","${p.category}","${p.supplierName || ''}",${p.unitPrice},${p.currentStock},${val},"${db.getProductStatus(p)}"\n`;
        });
    } else if (reportType === 'low-stock') {
        csv = 'Product Code,Product Name,Category,Current Stock,Reorder Level,Shortage,Status\n';
        db.products.filter(p => p.currentStock <= p.minimumStock).forEach(p => {
            csv += `"${p.productCode}","${p.name}","${p.category}",${p.currentStock},${p.minimumStock},${p.minimumStock - p.currentStock},"${db.getProductStatus(p)}"\n`;
        });
    } else if (reportType === 'suppliers') {
        csv = 'Supplier Code,Company Name,Contact Person,Phone,Email,Lead Time Days,Reliability %,Status\n';
        db.suppliers.forEach(s => {
            csv += `"${s.supplierCode}","${s.name}","${s.contactPerson}","${s.phone}","${s.email}",${s.leadTime},${s.reliabilityScore || 95},"${s.status}"\n`;
        });
    } else if (reportType === 'stock-movement') {
        csv = 'Transaction ID,Date,Product Code,Product Name,Type,Quantity,Reference,Agent\n';
        db.transactions.forEach(t => {
            csv += `${t.id},"${t.transactionDate}","${t.productCode}","${t.productName}","${t.type}",${t.quantity},"${t.reason}","${t.performedBy}"\n`;
        });
    }

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `SIMR_${reportType}_report_${new Date().toISOString().split('T')[0]}.csv`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    showToast('Report CSV successfully generated and downloaded.', 'success');
}

// =============================================================================
// 16. MODALS & FORMS
// =============================================================================
function initModals() {
    document.querySelectorAll('[data-close-modal]').forEach(btn => {
        btn.addEventListener('click', () => {
            playAudioFx('click');
            closeAllModals();
        });
    });

    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) {
                playAudioFx('click');
                closeAllModals();
            }
        });
    });
}

function closeAllModals() {
    document.querySelectorAll('.modal-overlay').forEach(o => o.classList.remove('show'));
}

function openModal(modalId) {
    playAudioFx('click');
    closeAllModals();
    const m = document.getElementById(modalId);
    if (m) m.classList.add('show');
}

// Add / Edit Product
function openAddProductModal() {
    document.getElementById('modalProductTitle').textContent = 'Add New Product SKU';
    document.getElementById('productModalId').value = '0';
    document.getElementById('prodCode').value = 'PRD-' + (1000 + db.products.length + 1);
    document.getElementById('prodName').value = '';
    document.getElementById('prodUnitPrice').value = '';
    document.getElementById('prodCurrentStock').value = '20';
    document.getElementById('prodMinimumStock').value = '10';
    document.getElementById('prodMaximumStock').value = '60';
    document.getElementById('prodReorderQuantity').value = '25';
    populateSupplierDropdowns(db.suppliers);
    openModal('modalProduct');
}

function openEditProductModal(id) {
    const p = db.products.find(x => x.id === id);
    if (!p) return;
    document.getElementById('modalProductTitle').textContent = 'Edit Product // ' + p.productCode;
    document.getElementById('productModalId').value = p.id;
    document.getElementById('prodCode').value = p.productCode;
    document.getElementById('prodName').value = p.name;
    document.getElementById('prodCategory').value = p.category;
    document.getElementById('prodUnitPrice').value = p.unitPrice;
    document.getElementById('prodCurrentStock').value = p.currentStock;
    document.getElementById('prodMinimumStock').value = p.minimumStock;
    document.getElementById('prodMaximumStock').value = p.maximumStock;
    document.getElementById('prodReorderQuantity').value = p.reorderQuantity;
    populateSupplierDropdowns(db.suppliers);
    document.getElementById('prodSupplier').value = p.supplierId || 0;
    openModal('modalProduct');
}

function openViewProductModal(id) {
    const p = db.products.find(x => x.id === id);
    if (!p) return;
    const body = document.getElementById('viewProductBody');
    const st = db.getProductStatus(p);
    const badgeClass = st === 'Out of Stock' ? 'badge-out' : (st === 'Critical' ? 'badge-critical' : 'badge-normal');
    const pct = Math.min(100, Math.round((p.currentStock / (p.maximumStock || 100)) * 100));

    body.innerHTML = `
        <div style="text-align: center; margin-bottom: 20px;">
            <div style="font-size: 1.25rem; font-weight: 700;">${escapeHtml(p.name)}</div>
            <div style="display: flex; justify-content: center; gap: 8px; margin-top: 6px;">
                <span class="badge-code">${escapeHtml(p.productCode)}</span>
                <span class="badge ${badgeClass}">${st}</span>
            </div>
        </div>

        <div style="background: rgba(0, 0, 0, 0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 16px; margin-bottom: 20px; text-align: center;">
            <div style="font-size: 0.75rem; color: var(--cyan-neon); font-family: var(--font-mono); margin-bottom: 8px;">OPTICAL CODE 128 BARCODE</div>
            ${generateBarcodeSvg(p.productCode)}
        </div>

        <div class="form-grid-2" style="margin-bottom: 16px;">
            <div style="padding: 10px; background: rgba(0, 242, 254, 0.05); border-radius: var(--radius-sm);">
                <span style="font-size: 0.75rem; color: var(--text-muted);">Current Stock:</span>
                <div style="font-size: 1.25rem; font-weight: 700; font-family: var(--font-mono);">${p.currentStock} Units</div>
            </div>
            <div style="padding: 10px; background: rgba(0, 242, 254, 0.05); border-radius: var(--radius-sm);">
                <span style="font-size: 0.75rem; color: var(--text-muted);">Unit Valuation:</span>
                <div style="font-size: 1.25rem; font-weight: 700; font-family: var(--font-mono); color: var(--cyan-neon);">${formatCurrency(p.unitPrice)}</div>
            </div>
        </div>

        <div style="margin-bottom: 16px;">
            <div style="display: flex; justify-content: space-between; font-size: 0.8125rem; margin-bottom: 4px;">
                <span>Rack Capacity Utilization</span>
                <strong>${pct}% (${p.currentStock}/${p.maximumStock || 100})</strong>
            </div>
            <div style="height: 8px; background: rgba(255,255,255,0.1); border-radius: 4px; overflow: hidden;">
                <div style="width: ${pct}%; height: 100%; background: var(--cyan-neon); box-shadow: 0 0 10px var(--cyan-neon);"></div>
            </div>
        </div>

        <div style="display: flex; justify-content: space-between; font-size: 0.8125rem; color: var(--text-muted);">
            <span>Assigned Supplier: <strong>${escapeHtml(p.supplierName || 'None')}</strong></span>
            <span>Warehouse Rack: <strong>${escapeHtml(p.rackBay || 'A-1')}</strong></span>
        </div>
    `;

    openModal('modalViewProduct');
}

// Add / Edit Supplier
function openAddSupplierModal() {
    document.getElementById('modalSupplierTitle').textContent = 'Add Verified Supplier';
    document.getElementById('supplierModalId').value = '0';
    document.getElementById('supCode').value = 'SUP-00' + (db.suppliers.length + 1);
    document.getElementById('supName').value = '';
    document.getElementById('supContact').value = '';
    document.getElementById('supPhone').value = '';
    document.getElementById('supEmail').value = '';
    document.getElementById('supLeadTime').value = '3';
    document.getElementById('supAddress').value = '';
    openModal('modalSupplier');
}

function openEditSupplierModal(id) {
    const s = db.suppliers.find(x => x.id === id);
    if (!s) return;
    document.getElementById('modalSupplierTitle').textContent = 'Edit Supplier // ' + s.supplierCode;
    document.getElementById('supplierModalId').value = s.id;
    document.getElementById('supCode').value = s.supplierCode;
    document.getElementById('supName').value = s.name;
    document.getElementById('supContact').value = s.contactPerson;
    document.getElementById('supPhone').value = s.phone;
    document.getElementById('supEmail').value = s.email;
    document.getElementById('supLeadTime').value = s.leadTime;
    document.getElementById('supAddress').value = s.address;
    document.getElementById('supStatus').value = s.status;
    openModal('modalSupplier');
}

// Create Reorder PO Modal
function openReorderModalFromRec(productId) {
    const p = db.products.find(x => x.id === productId);
    if (!p) return;
    const sup = db.suppliers.find(s => s.id === p.supplierId) || { id: 1, name: 'General Supplier' };

    document.getElementById('poProductId').value = p.id;
    document.getElementById('poSupplierId').value = sup.id;
    document.getElementById('poProductName').textContent = p.name + ` (${p.productCode})`;
    document.getElementById('poSupplierName').textContent = sup.name;
    document.getElementById('poCurrentStock').textContent = `${p.currentStock} units (ROP: ${p.minimumStock})`;

    const qty = Math.max(p.reorderQuantity || 25, (p.maximumStock || 60) - p.currentStock);
    document.getElementById('poQuantity').value = qty;
    document.getElementById('poUnitPrice').value = p.unitPrice;

    const calcTotal = () => {
        const q = Number(document.getElementById('poQuantity').value || 0);
        const up = Number(document.getElementById('poUnitPrice').value || 0);
        document.getElementById('poEstimatedTotal').textContent = formatCurrency(q * up);
    };

    document.getElementById('poQuantity').oninput = calcTotal;
    document.getElementById('poUnitPrice').oninput = calcTotal;
    calcTotal();

    openModal('modalReorder');
}

// Printable PO Invoice Modal
function openPOInvoiceModal(poId) {
    const po = db.purchaseOrders.find(x => x.id === poId);
    if (!po) return;
    const body = document.getElementById('poInvoiceBody');
    const sup = db.suppliers.find(s => s.id === po.supplierId) || { name: po.supplierName, address: 'Verified Vendor Logistics Hub', email: 'vendor@simr-network.io' };
    const dateStr = new Date(po.orderDate || Date.now()).toLocaleDateString('en-IN', { year: 'numeric', month: 'long', day: 'numeric' });

    body.innerHTML = `
        <div style="padding: 20px; background: #ffffff; color: #0f172a; border-radius: 8px; font-family: 'Inter', sans-serif;">
            <div style="display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 2px solid #00f2fe; padding-bottom: 16px; margin-bottom: 20px;">
                <div>
                    <h2 style="font-size: 1.5rem; font-weight: 800; color: #0f172a; margin-bottom: 4px;">SIMR LOGISTICS CORP.</h2>
                    <p style="font-size: 0.8125rem; color: #64748b; margin: 0;">Smart Automated Inventory &amp; Predictive Procurement Network</p>
                    <p style="font-size: 0.8125rem; color: #64748b; margin: 0;">Silicon Bay Tech Center, Building 4 // License #SIMR-2026-CAP</p>
                </div>
                <div style="text-align: right;">
                    <div style="font-size: 1.25rem; font-weight: 700; color: #0284c7; font-family: 'JetBrains Mono', monospace;">${escapeHtml(po.poNumber)}</div>
                    <div style="font-size: 0.8125rem; color: #64748b;">Date: ${dateStr}</div>
                    <span style="display: inline-block; padding: 2px 8px; font-size: 0.75rem; font-weight: 700; border-radius: 4px; background: #e0f2fe; color: #0369a1; margin-top: 4px;">${po.status || 'PENDING APPROVAL'}</span>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 24px; font-size: 0.875rem;">
                <div style="background: #f8fafc; padding: 12px; border-radius: 6px; border: 1px solid #e2e8f0;">
                    <strong style="color: #475569; font-size: 0.75rem; text-transform: uppercase;">VENDOR / SUPPLIER:</strong>
                    <div style="font-weight: 700; margin-top: 4px; font-size: 1rem;">${escapeHtml(sup.name)}</div>
                    <div style="color: #64748b; font-size: 0.8125rem;">${escapeHtml(sup.address || '')}</div>
                    <div style="color: #64748b; font-size: 0.8125rem;">Email: ${escapeHtml(sup.email || '')}</div>
                </div>
                <div style="background: #f8fafc; padding: 12px; border-radius: 6px; border: 1px solid #e2e8f0;">
                    <strong style="color: #475569; font-size: 0.75rem; text-transform: uppercase;">SHIP TO WAREHOUSE:</strong>
                    <div style="font-weight: 700; margin-top: 4px; font-size: 1rem;">Central Fulfillment Bay Alpha</div>
                    <div style="color: #64748b; font-size: 0.8125rem;">Dock 04, East Industrial Hub, Sector 12</div>
                    <div style="color: #64748b; font-size: 0.8125rem;">Expected Arrival: <strong>${po.expectedDelivery || 'Standard 3 Days'}</strong></div>
                </div>
            </div>

            <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px; font-size: 0.875rem;">
                <thead>
                    <tr style="background: #f1f5f9; text-align: left;">
                        <th style="padding: 10px; border-bottom: 1px solid #cbd5e1;">Item Description</th>
                        <th style="padding: 10px; border-bottom: 1px solid #cbd5e1; text-align: right;">Quantity</th>
                        <th style="padding: 10px; border-bottom: 1px solid #cbd5e1; text-align: right;">Unit Price</th>
                        <th style="padding: 10px; border-bottom: 1px solid #cbd5e1; text-align: right;">Line Total</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="padding: 12px 10px; border-bottom: 1px solid #e2e8f0;">
                            <strong>${escapeHtml(po.productName)}</strong>
                            <div style="font-size: 0.75rem; color: #64748b; font-family: monospace;">SKU: PRD-${po.productId || '1001'}</div>
                        </td>
                        <td style="padding: 12px 10px; border-bottom: 1px solid #e2e8f0; text-align: right; font-weight: 700; font-family: monospace;">${po.quantity}</td>
                        <td style="padding: 12px 10px; border-bottom: 1px solid #e2e8f0; text-align: right; font-family: monospace;">${formatCurrency(po.unitPrice)}</td>
                        <td style="padding: 12px 10px; border-bottom: 1px solid #e2e8f0; text-align: right; font-weight: 700; font-family: monospace;">${formatCurrency(po.totalAmount)}</td>
                    </tr>
                </tbody>
            </table>

            <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-top: 24px; padding-top: 16px; border-top: 1px solid #e2e8f0;">
                <div style="display: flex; align-items: center; gap: 12px;">
                    ${generateQrCodeSvg(po.poNumber + '|' + po.totalAmount)}
                    <div style="font-size: 0.6875rem; color: #64748b;">
                        SCAN TO VERIFY INTEGRITY<br>
                        Digital Hash Verified // SIMR Blockchain Key
                    </div>
                </div>

                <div style="width: 220px; text-align: right;">
                    <div style="display: flex; justify-content: space-between; margin-bottom: 4px; font-size: 0.8125rem;">
                        <span style="color: #64748b;">Subtotal:</span>
                        <span>${formatCurrency(po.totalAmount)}</span>
                    </div>
                    <div style="display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 0.8125rem;">
                        <span style="color: #64748b;">Taxes (GST 18%):</span>
                        <span>Included</span>
                    </div>
                    <div style="display: flex; justify-content: space-between; padding-top: 8px; border-top: 2px solid #0f172a; font-weight: 800; font-size: 1.125rem; color: #0284c7;">
                        <span>Total Due:</span>
                        <span style="font-family: monospace;">${formatCurrency(po.totalAmount)}</span>
                    </div>
                </div>
            </div>

            <div style="margin-top: 32px; padding-top: 16px; border-top: 1px dashed #cbd5e1; display: flex; justify-content: space-between; font-size: 0.75rem; color: #64748b;">
                <div>Authorized Signature: _______________________</div>
                <div>Automated System Verification Date: ${dateStr}</div>
            </div>
        </div>
    `;

    openModal('modalPOInvoice');
}

// Printable Barcodes Sheet
function openPrintableBarcodesModal() {
    const body = document.getElementById('barcodeSheetBody');
    let cards = '';

    db.products.forEach(p => {
        cards += `
            <div style="padding: 14px; background: #fff; color: #000; border: 1px solid #ccc; border-radius: 6px; text-align: center; margin-bottom: 14px; break-inside: avoid;">
                <div style="font-weight: 700; font-size: 0.875rem;">${escapeHtml(p.name)}</div>
                <div style="font-size: 0.75rem; color: #666; margin-bottom: 4px;">SKU: ${escapeHtml(p.productCode)} | Price: ${formatCurrency(p.unitPrice)}</div>
                ${generateBarcodeSvg(p.productCode)}
            </div>
        `;
    });

    body.innerHTML = `
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
            ${cards}
        </div>
    `;

    openModal('modalBarcodeLabels');
}

// Confirm Delete
function confirmDeleteProduct(id, name) {
    state.deleteTarget = { type: 'product', id: id, name: name };
    document.getElementById('deleteConfirmMessage').innerHTML = `Permanently delete product SKU: <strong>${escapeHtml(name)}</strong>?`;
    openModal('modalDeleteConfirm');
}

function confirmDeleteSupplier(id, name) {
    state.deleteTarget = { type: 'supplier', id: id, name: name };
    document.getElementById('deleteConfirmMessage').innerHTML = `Permanently delete supplier: <strong>${escapeHtml(name)}</strong>?`;
    openModal('modalDeleteConfirm');
}

// =============================================================================
// 17. EVENT LISTENERS & LASER SCANNER / VOICE ASSISTANT
// =============================================================================
function initEventListeners() {
    // Topbar Laser Scanner Button
    document.getElementById('btnLaserScanner')?.addEventListener('click', openLaserScannerModal);
    document.getElementById('quickBtnScan')?.addEventListener('click', openLaserScannerModal);
    document.getElementById('btnScanForStockOut')?.addEventListener('click', openLaserScannerModal);

    // Topbar Voice Assistant Button
    document.getElementById('btnVoiceAssistant')?.addEventListener('click', openVoiceAssistantModal);
    document.getElementById('quickBtnVoice')?.addEventListener('click', openVoiceAssistantModal);

    // Print all barcodes
    document.getElementById('btnPrintAllBarcodes')?.addEventListener('click', openPrintableBarcodesModal);

    // Export CSV
    document.getElementById('btnExportReportCsv')?.addEventListener('click', exportActiveReportCsv);

    // Add Product & Supplier buttons
    document.getElementById('btnOpenAddProduct')?.addEventListener('click', openAddProductModal);
    document.getElementById('btnOpenAddSupplier')?.addEventListener('click', openAddSupplierModal);

    // Search and Filters
    document.getElementById('inventorySearchInput')?.addEventListener('input', loadInventoryView);
    document.getElementById('inventoryCategoryFilter')?.addEventListener('change', loadInventoryView);
    document.getElementById('inventoryStatusFilter')?.addEventListener('change', loadInventoryView);

    // Warehouse Zone Filter
    document.getElementById('warehouseZoneNav')?.addEventListener('click', (e) => {
        if (e.target.classList.contains('zone-filter-btn')) {
            playAudioFx('click');
            document.querySelectorAll('.zone-filter-btn').forEach(b => b.classList.remove('active'));
            e.target.classList.add('active');
            state.activeZone = e.target.getAttribute('data-zone') || 'ALL';
            loadWarehouseView();
        }
    });

    // Report tabs
    document.querySelectorAll('.report-tab-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            playAudioFx('click');
            const type = btn.getAttribute('data-report');
            loadReportView(type);
        });
    });

    // Form: Save Product (Add/Edit)
    document.getElementById('formProductModal')?.addEventListener('submit', (e) => {
        e.preventDefault();
        playAudioFx('success');

        const id = Number(document.getElementById('productModalId').value || 0);
        const code = document.getElementById('prodCode').value.trim();
        const name = document.getElementById('prodName').value.trim();
        const category = document.getElementById('prodCategory').value;
        const supId = Number(document.getElementById('prodSupplier').value || 0);
        const unitPrice = parseFloat(document.getElementById('prodUnitPrice').value || 0);
        const currentStock = parseInt(document.getElementById('prodCurrentStock').value || 0);
        const minStock = parseInt(document.getElementById('prodMinimumStock').value || 10);
        const maxStock = parseInt(document.getElementById('prodMaximumStock').value || 60);
        const reorderQty = parseInt(document.getElementById('prodReorderQuantity').value || 25);

        const sup = db.suppliers.find(s => s.id === supId);
        const supName = sup ? sup.name : 'Unassigned';

        if (id > 0) {
            const p = db.products.find(x => x.id === id);
            if (p) {
                p.productCode = code;
                p.name = name;
                p.category = category;
                p.supplierId = supId;
                p.supplierName = supName;
                p.unitPrice = unitPrice;
                p.currentStock = currentStock;
                p.minimumStock = minStock;
                p.maximumStock = maxStock;
                p.reorderQuantity = reorderQty;
            }
            showToast(`Product ${code} updated successfully.`, 'success');
        } else {
            const newP = {
                id: db.products.length ? Math.max(...db.products.map(x => x.id)) + 1 : 1,
                productCode: code,
                name: name,
                category: category,
                supplierId: supId,
                supplierName: supName,
                unitPrice: unitPrice,
                currentStock: currentStock,
                minimumStock: minStock,
                maximumStock: maxStock,
                reorderQuantity: reorderQty,
                rackBay: 'A-' + (Math.floor(Math.random() * 4) + 1)
            };
            db.products.push(newP);
            showToast(`Product ${code} registered in catalog.`, 'success');
        }

        db.save();
        closeAllModals();
        loadAllData();
        refreshView(state.currentView);
    });

    // Form: Save Supplier (Add/Edit)
    document.getElementById('formSupplierModal')?.addEventListener('submit', (e) => {
        e.preventDefault();
        playAudioFx('success');

        const id = Number(document.getElementById('supplierModalId').value || 0);
        const code = document.getElementById('supCode').value.trim();
        const name = document.getElementById('supName').value.trim();
        const contact = document.getElementById('supContact').value.trim();
        const phone = document.getElementById('supPhone').value.trim();
        const email = document.getElementById('supEmail').value.trim();
        const leadTime = parseInt(document.getElementById('supLeadTime').value || 3);
        const address = document.getElementById('supAddress').value.trim();
        const status = document.getElementById('supStatus').value;

        if (id > 0) {
            const s = db.suppliers.find(x => x.id === id);
            if (s) {
                s.supplierCode = code;
                s.name = name;
                s.contactPerson = contact;
                s.phone = phone;
                s.email = email;
                s.leadTime = leadTime;
                s.address = address;
                s.status = status;
            }
            showToast(`Supplier ${code} updated.`, 'success');
        } else {
            const newS = {
                id: db.suppliers.length ? Math.max(...db.suppliers.map(x => x.id)) + 1 : 1,
                supplierCode: code,
                name: name,
                contactPerson: contact,
                phone: phone,
                email: email,
                leadTime: leadTime,
                reliabilityScore: 95,
                address: address,
                status: status
            };
            db.suppliers.push(newS);
            showToast(`Supplier ${code} onboarded successfully.`, 'success');
        }

        db.save();
        closeAllModals();
        loadAllData();
        refreshView(state.currentView);
    });

    // Form: Stock IN (Replenishment)
    document.getElementById('formStockIn')?.addEventListener('submit', (e) => {
        e.preventDefault();
        const pid = Number(document.getElementById('stockInProductSelect').value);
        const qty = Number(document.getElementById('stockInQuantity').value);
        const reason = document.getElementById('stockInReason').value.trim() || 'Replenishment Intake';

        const prod = db.products.find(x => x.id === pid);
        if (!prod || qty <= 0) return;

        prod.currentStock += qty;
        db.transactions.unshift({
            id: db.transactions.length ? Math.max(...db.transactions.map(x => x.id)) + 1 : 1,
            productId: prod.id,
            productCode: prod.productCode,
            productName: prod.name,
            type: 'IN',
            quantity: qty,
            reason: reason,
            performedBy: state.currentUser ? state.currentUser.username : 'admin',
            transactionDate: new Date().toISOString()
        });

        db.save();
        playAudioFx('success');
        showToast(`Replenished +${qty} units of ${prod.name}`, 'success');
        document.getElementById('formStockIn').reset();
        loadAllData();
        refreshView(state.currentView);
    });

    // Form: Stock OUT (Dispatch with Negative Stock Protection)
    document.getElementById('formStockOut')?.addEventListener('submit', (e) => {
        e.preventDefault();
        const pid = Number(document.getElementById('stockOutProductSelect').value);
        const qty = Number(document.getElementById('stockOutQuantity').value);
        const reason = document.getElementById('stockOutReason').value.trim() || 'Sales Dispatch';

        const prod = db.products.find(x => x.id === pid);
        if (!prod || qty <= 0) return;

        if (prod.currentStock < qty) {
            playAudioFx('alert');
            showToast(`Negative stock guard: Only ${prod.currentStock} units available in stock!`, 'error');
            return;
        }

        prod.currentStock -= qty;
        db.transactions.unshift({
            id: db.transactions.length ? Math.max(...db.transactions.map(x => x.id)) + 1 : 1,
            productId: prod.id,
            productCode: prod.productCode,
            productName: prod.name,
            type: 'OUT',
            quantity: qty,
            reason: reason,
            performedBy: state.currentUser ? state.currentUser.username : 'admin',
            transactionDate: new Date().toISOString()
        });

        db.save();
        playAudioFx('laser');
        showToast(`Dispatched -${qty} units of ${prod.name}`, 'success');
        document.getElementById('formStockOut').reset();
        document.getElementById('stockOutAvailableNotice').textContent = 'Available: --';
        loadAllData();
        refreshView(state.currentView);
    });

    // Stock OUT product change available stock indicator
    document.getElementById('stockOutProductSelect')?.addEventListener('change', (e) => {
        const pid = Number(e.target.value);
        const prod = db.products.find(x => x.id === pid);
        const notice = document.getElementById('stockOutAvailableNotice');
        if (notice) {
            notice.textContent = prod ? `Available: ${prod.currentStock} Units` : 'Available: --';
        }
    });

    // Form: Create PO
    document.getElementById('formCreatePO')?.addEventListener('submit', (e) => {
        e.preventDefault();
        playAudioFx('success');

        const pid = Number(document.getElementById('poProductId').value);
        const supId = Number(document.getElementById('poSupplierId').value);
        const qty = Number(document.getElementById('poQuantity').value);
        const price = parseFloat(document.getElementById('poUnitPrice').value);

        const prod = db.products.find(x => x.id === pid);
        const sup = db.suppliers.find(s => s.id === supId);
        if (!prod || qty <= 0) return;

        const poNum = `PO-2026-${String(db.purchaseOrders.length + 1).padStart(3, '0')}`;
        const leadDays = (sup && sup.leadTime) ? sup.leadTime : 3;
        const deliveryDate = new Date(Date.now() + leadDays * 86400000).toISOString().split('T')[0];

        const newPo = {
            id: db.purchaseOrders.length ? Math.max(...db.purchaseOrders.map(x => x.id)) + 1 : 1,
            poNumber: poNum,
            supplierId: supId,
            supplierName: sup ? sup.name : 'General Vendor',
            productId: prod.id,
            productName: prod.name,
            quantity: qty,
            unitPrice: price,
            totalAmount: Math.round(qty * price * 100) / 100,
            orderDate: new Date().toISOString(),
            expectedDelivery: deliveryDate,
            status: 'Pending'
        };

        db.purchaseOrders.unshift(newPo);
        db.save();

        closeAllModals();
        showToast(`Purchase Order ${poNum} successfully generated!`, 'success');
        loadAllData();
        refreshView(state.currentView);
    });

    // Confirm Delete Action
    document.getElementById('btnConfirmDeleteAction')?.addEventListener('click', () => {
        playAudioFx('alert');
        const target = state.deleteTarget;
        if (target.type === 'product') {
            db.products = db.products.filter(p => p.id !== target.id);
            showToast(`Product deleted from inventory.`, 'info');
        } else if (target.type === 'supplier') {
            db.suppliers = db.suppliers.filter(s => s.id !== target.id);
            showToast(`Supplier removed from registry.`, 'info');
        }
        db.save();
        closeAllModals();
        loadAllData();
        refreshView(state.currentView);
    });

    // AI Sandbox Interactive Sliders
    const sDemand = document.getElementById('sliderDemand');
    const sLead = document.getElementById('sliderLeadDelay');
    const sBuffer = document.getElementById('sliderSafetyBuffer');

    const updateAiSimulation = () => {
        state.simulationParams.demandMultiplier = parseFloat(sDemand.value);
        state.simulationParams.leadTimeDelay = parseInt(sLead.value);
        state.simulationParams.safetyBufferFactor = parseFloat(sBuffer.value);

        document.getElementById('valDemandMultiplier').textContent = `${sDemand.value}x (${Math.round((sDemand.value - 1) * 100)}% Surge)`;
        document.getElementById('valLeadTimeDelay').textContent = `+${sLead.value} Days Delay`;
        document.getElementById('valSafetyBuffer').textContent = `${sBuffer.value}x Buffer`;

        loadReorderView();
    };

    sDemand?.addEventListener('input', updateAiSimulation);
    sLead?.addEventListener('input', updateAiSimulation);
    sBuffer?.addEventListener('input', updateAiSimulation);

    document.getElementById('btnResetSimulation')?.addEventListener('click', () => {
        playAudioFx('click');
        if (sDemand) sDemand.value = '1.0';
        if (sLead) sLead.value = '0';
        if (sBuffer) sBuffer.value = '1.0';
        updateAiSimulation();
        showToast('AI Simulation reset to standard baseline demand.', 'info');
    });
}

// Populate Dropdowns
function populateProductDropdowns(products) {
    const inSelect = document.getElementById('stockInProductSelect');
    const outSelect = document.getElementById('stockOutProductSelect');
    const scanSelect = document.getElementById('scannerSkuSelect');

    let opts = '<option value="">-- Choose a product SKU --</option>';
    products.forEach(p => {
        opts += `<option value="${p.id}">${escapeHtml(p.productCode)} - ${escapeHtml(p.name)} (${p.currentStock} in stock)</option>`;
    });

    if (inSelect) inSelect.innerHTML = opts;
    if (outSelect) outSelect.innerHTML = opts;
    if (scanSelect) scanSelect.innerHTML = opts;
}

function populateSupplierDropdowns(suppliers) {
    const supSelect = document.getElementById('prodSupplier');
    if (!supSelect) return;
    let opts = '<option value="0">-- Select Supplier --</option>';
    suppliers.forEach(s => {
        opts += `<option value="${s.id}">${escapeHtml(s.supplierCode)} - ${escapeHtml(s.name)}</option>`;
    });
    supSelect.innerHTML = opts;
}

// =============================================================================
// 18. LASER BARCODE SCANNER HUD MODAL
// =============================================================================
function openLaserScannerModal() {
    populateProductDropdowns(db.products);
    document.getElementById('scannerResultBox').style.display = 'none';
    openModal('modalLaserScanner');
    playAudioFx('laser');

    document.getElementById('btnExecuteLaserScan').onclick = () => {
        const pid = Number(document.getElementById('scannerSkuSelect').value);
        if (!pid) {
            showToast('Select a product SKU to simulate optical scan', 'error');
            return;
        }
        executeSimulatedLaserScan(pid);
    };
}

function executeSimulatedLaserScan(productId) {
    const prod = db.products.find(p => p.id === productId);
    if (!prod) return;

    playAudioFx('laser');
    const resultBox = document.getElementById('scannerResultBox');
    document.getElementById('scannedProdName').textContent = prod.name;
    document.getElementById('scannedProdStock').textContent = `${prod.currentStock} in stock`;
    document.getElementById('scannedProdMeta').textContent = `SKU: ${prod.productCode} | Unit Price: ${formatCurrency(prod.unitPrice)} | Category: ${prod.category}`;
    resultBox.style.display = 'block';

    document.getElementById('btnQuickDispatchScanned').onclick = () => {
        if (prod.currentStock <= 0) {
            playAudioFx('alert');
            showToast('Stock is zero! Cannot dispatch.', 'error');
            return;
        }
        prod.currentStock -= 1;
        db.transactions.unshift({
            id: db.transactions.length ? Math.max(...db.transactions.map(x => x.id)) + 1 : 1,
            productId: prod.id,
            productCode: prod.productCode,
            productName: prod.name,
            type: 'OUT',
            quantity: 1,
            reason: 'Fast Laser Barcode Dispatch',
            performedBy: 'scanner',
            transactionDate: new Date().toISOString()
        });
        db.save();
        playAudioFx('laser');
        document.getElementById('scannedProdStock').textContent = `${prod.currentStock} in stock`;
        showToast(`Dispatched 1 unit of ${prod.productCode}`, 'success');
        loadAllData();
    };

    document.getElementById('btnQuickRestockScanned').onclick = () => {
        prod.currentStock += 10;
        db.transactions.unshift({
            id: db.transactions.length ? Math.max(...db.transactions.map(x => x.id)) + 1 : 1,
            productId: prod.id,
            productCode: prod.productCode,
            productName: prod.name,
            type: 'IN',
            quantity: 10,
            reason: 'Fast Laser Barcode Intake',
            performedBy: 'scanner',
            transactionDate: new Date().toISOString()
        });
        db.save();
        playAudioFx('success');
        document.getElementById('scannedProdStock').textContent = `${prod.currentStock} in stock`;
        showToast(`Replenished +10 units of ${prod.productCode}`, 'success');
        loadAllData();
    };
}

// =============================================================================
// 19. A.I.V.A. CYBER VOICE ASSISTANT (Speech Recognition & Synthesis)
// =============================================================================
let recognition = null;

function openVoiceAssistantModal() {
    openModal('modalVoiceAssistant');
    playAudioFx('success');

    const statusText = document.getElementById('voiceStatusText');
    const transcriptBox = document.getElementById('voiceTranscriptBox');

    document.getElementById('btnVoiceDemoSpeech').onclick = () => {
        speakVoiceText(`A.I.V.A. voice telemetry online. Monitoring ${db.products.length} product units with ${db.suppliers.length} active supply partners.`);
    };

    document.getElementById('btnStartVoiceListen').onclick = () => {
        startVoiceRecognition(statusText, transcriptBox);
    };
}

function speakVoiceText(text) {
    if ('speechSynthesis' in window) {
        window.speechSynthesis.cancel();
        const utterance = new SpeechSynthesisUtterance(text);
        utterance.pitch = 1.05;
        utterance.rate = 1.0;
        window.speechSynthesis.speak(utterance);
    }
}

function startVoiceRecognition(statusEl, transcriptEl) {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SpeechRecognition) {
        statusEl.textContent = 'Speech recognition not supported in this browser. Running synthesis demo instead.';
        speakVoiceText("Speech recognition API is unavailable, but audio synthesis is fully operational.");
        return;
    }

    if (recognition) {
        try { recognition.abort(); } catch (e) {}
    }

    recognition = new SpeechRecognition();
    recognition.lang = 'en-US';
    recognition.interimResults = false;
    recognition.maxAlternatives = 1;

    statusEl.textContent = 'Listening... Speak now!';
    playAudioFx('laser');

    recognition.onresult = (event) => {
        const transcript = event.results[0][0].transcript.toLowerCase();
        transcriptEl.textContent = `"${transcript}"`;
        handleVoiceCommand(transcript, statusEl);
    };

    recognition.onerror = (event) => {
        statusEl.textContent = 'Speech not recognized. Try again.';
    };

    recognition.onend = () => {};

    try {
        recognition.start();
    } catch (e) {}
}

function handleVoiceCommand(cmd, statusEl) {
    if (cmd.includes('alert') || cmd.includes('warning') || cmd.includes('low')) {
        navigate('dashboard');
        const low = db.products.filter(p => p.currentStock <= p.minimumStock).length;
        const msg = `Found ${low} products requiring replenishment.`;
        statusEl.textContent = msg;
        speakVoiceText(msg);
    } else if (cmd.includes('warehouse') || cmd.includes('twin') || cmd.includes('bay')) {
        navigate('warehouse');
        const msg = "Opening Warehouse Digital Twin spatial grid.";
        statusEl.textContent = msg;
        speakVoiceText(msg);
    } else if (cmd.includes('reorder') || cmd.includes('sandbox') || cmd.includes('predict')) {
        navigate('reorder');
        const msg = "Opening Predictive AI Reorder Simulator.";
        statusEl.textContent = msg;
        speakVoiceText(msg);
    } else if (cmd.includes('catalog') || cmd.includes('inventory') || cmd.includes('product')) {
        navigate('inventory');
        const msg = `Navigating to inventory catalog with ${db.products.length} registered SKUs.`;
        statusEl.textContent = msg;
        speakVoiceText(msg);
    } else if (cmd.includes('theme') || cmd.includes('dark') || cmd.includes('light')) {
        document.getElementById('btnToggleTheme')?.click();
        const msg = "Switching visual theme.";
        statusEl.textContent = msg;
        speakVoiceText(msg);
    } else if (cmd.includes('status') || cmd.includes('system') || cmd.includes('health')) {
        const stats = db.getStats();
        const msg = `System status is optimal. Total inventory value is ${Math.round(stats.inventoryValue)} rupees.`;
        statusEl.textContent = msg;
        speakVoiceText(msg);
    } else {
        const msg = `Processed instruction: ${cmd}`;
        statusEl.textContent = msg;
        speakVoiceText("Command received.");
    }
}

// =============================================================================
// 20. UTILITIES
// =============================================================================
function formatCurrency(val) {
    const num = Number(val || 0);
    return '₹' + num.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function capitalize(s) {
    if (!s) return '';
    return s.charAt(0).toUpperCase() + s.slice(1);
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

function escapeJs(str) {
    if (!str) return '';
    return String(str).replace(/'/g, "\\'").replace(/"/g, '\\"');
}

function showToast(message, type = 'info') {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <span style="font-size: 1rem;">${type === 'success' ? '⚡' : (type === 'error' ? '⚠️' : 'ℹ️')}</span>
        <span>${escapeHtml(message)}</span>
    `;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(10px)';
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}
