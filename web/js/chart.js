/**
 * SIMR - Next-Gen Futuristic Stock Movement Overview Chart Renderer
 * Glowing Dual-Curve Canvas Chart with Interactive Crosshairs, Tooltips & Neon Gradients
 */

class StockMovementChart {
    constructor(canvasId) {
        this.canvas = document.getElementById(canvasId);
        if (!this.canvas) return;
        this.ctx = this.canvas.getContext('2d');
        this.data = [];
        this.hoverIndex = -1;
        this.mouseX = 0;
        this.mouseY = 0;
        this.init();
    }

    init() {
        this.resize();
        window.addEventListener('resize', () => {
            this.resize();
            this.render();
        });

        // Hover crosshairs & tooltip tracking
        this.canvas.addEventListener('mousemove', (e) => {
            const rect = this.canvas.getBoundingClientRect();
            this.mouseX = e.clientX - rect.left;
            this.mouseY = e.clientY - rect.top;
            this.updateHover();
        });

        this.canvas.addEventListener('mouseleave', () => {
            this.hoverIndex = -1;
            this.render();
        });
    }

    resize() {
        if (!this.canvas) return;
        const rect = this.canvas.parentElement.getBoundingClientRect();
        const dpr = window.devicePixelRatio || 1;
        this.width = rect.width || 600;
        this.height = 280;

        this.canvas.width = this.width * dpr;
        this.canvas.height = this.height * dpr;
        this.canvas.style.width = this.width + 'px';
        this.canvas.style.height = this.height + 'px';

        this.ctx.setTransform(1, 0, 0, 1, 0, 0); // reset transform
        this.ctx.scale(dpr, dpr);
    }

    setData(movements) {
        this.data = movements || [];
        this.render();
    }

    updateHover() {
        if (!this.data || this.data.length === 0) return;
        const padding = { top: 35, right: 35, bottom: 45, left: 55 };
        const chartW = this.width - padding.left - padding.right;
        const pointCount = this.data.length;
        const stepX = chartW / Math.max(1, pointCount - 1);

        let closestIdx = -1;
        let minDist = Infinity;

        for (let i = 0; i < pointCount; i++) {
            const x = padding.left + (pointCount === 1 ? chartW / 2 : stepX * i);
            const dist = Math.abs(x - this.mouseX);
            if (dist < minDist) {
                minDist = dist;
                closestIdx = i;
            }
        }

        if (minDist < stepX * 0.8) {
            this.hoverIndex = closestIdx;
        } else {
            this.hoverIndex = -1;
        }
        this.render();
    }

    render() {
        if (!this.canvas || !this.ctx) return;
        const ctx = this.ctx;
        const w = this.width;
        const h = this.height;
        const isDark = document.body.classList.contains('cyber-dark') || !document.body.classList.contains('cyber-light');

        ctx.clearRect(0, 0, w, h);

        if (!this.data || this.data.length === 0) {
            ctx.fillStyle = isDark ? '#64748b' : '#94a3b8';
            ctx.font = '500 14px "Space Grotesk", Inter, sans-serif';
            ctx.textAlign = 'center';
            ctx.fillText('NO STOCK MOVEMENT TELEMETRY AVAILABLE', w / 2, h / 2);
            return;
        }

        const padding = { top: 35, right: 35, bottom: 45, left: 55 };
        const chartW = w - padding.left - padding.right;
        const chartH = h - padding.top - padding.bottom;

        // Find max value
        let maxVal = 10;
        this.data.forEach(d => {
            const inQty = Number(d.stockIn || 0);
            const outQty = Number(d.stockOut || 0);
            if (inQty > maxVal) maxVal = inQty;
            if (outQty > maxVal) maxVal = outQty;
        });
        maxVal = Math.ceil(maxVal * 1.25 / 10) * 10;

        // Draw futuristic grid lines
        const gridLines = 4;
        ctx.lineWidth = 1;
        ctx.strokeStyle = isDark ? 'rgba(0, 242, 254, 0.08)' : 'rgba(226, 232, 240, 0.8)';
        ctx.fillStyle = isDark ? '#64748b' : '#94a3b8';
        ctx.font = '11px "JetBrains Mono", monospace';
        ctx.textAlign = 'right';

        for (let i = 0; i <= gridLines; i++) {
            const y = padding.top + (chartH / gridLines) * i;
            const val = Math.round(maxVal - (maxVal / gridLines) * i);

            ctx.beginPath();
            ctx.moveTo(padding.left, y);
            ctx.lineTo(w - padding.right, y);
            ctx.stroke();

            ctx.fillText(val.toString().padStart(2, '0') + ' U', padding.left - 10, y + 4);
        }

        const pointCount = this.data.length;
        const stepX = chartW / Math.max(1, pointCount - 1);
        const getX = index => padding.left + (pointCount === 1 ? chartW / 2 : stepX * index);
        const getY = val => padding.top + chartH - (Number(val || 0) / maxVal) * chartH;

        // 1. Stock IN Area & Glowing Line (Neon Emerald / Cyan)
        const neonIn = '#10b981';
        const neonInGlow = 'rgba(16, 185, 129, 0.4)';

        // Fill Area
        ctx.save();
        ctx.beginPath();
        ctx.moveTo(getX(0), padding.top + chartH);
        for (let i = 0; i < pointCount; i++) {
            ctx.lineTo(getX(i), getY(this.data[i].stockIn));
        }
        ctx.lineTo(getX(pointCount - 1), padding.top + chartH);
        ctx.closePath();
        const gradIn = ctx.createLinearGradient(0, padding.top, 0, padding.top + chartH);
        gradIn.addColorStop(0, 'rgba(16, 185, 129, 0.30)');
        gradIn.addColorStop(0.6, 'rgba(16, 185, 129, 0.08)');
        gradIn.addColorStop(1, 'rgba(16, 185, 129, 0.00)');
        ctx.fillStyle = gradIn;
        ctx.fill();
        ctx.restore();

        // Stroke Line
        ctx.save();
        ctx.shadowColor = neonInGlow;
        ctx.shadowBlur = 10;
        ctx.strokeStyle = neonIn;
        ctx.lineWidth = 3;
        ctx.beginPath();
        for (let i = 0; i < pointCount; i++) {
            const x = getX(i);
            const y = getY(this.data[i].stockIn);
            if (i === 0) ctx.moveTo(x, y);
            else ctx.lineTo(x, y);
        }
        ctx.stroke();
        ctx.restore();

        // 2. Stock OUT Area & Glowing Line (Neon Rose / Coral)
        const neonOut = '#f43f5e';
        const neonOutGlow = 'rgba(244, 63, 94, 0.4)';

        ctx.save();
        ctx.beginPath();
        ctx.moveTo(getX(0), padding.top + chartH);
        for (let i = 0; i < pointCount; i++) {
            ctx.lineTo(getX(i), getY(this.data[i].stockOut));
        }
        ctx.lineTo(getX(pointCount - 1), padding.top + chartH);
        ctx.closePath();
        const gradOut = ctx.createLinearGradient(0, padding.top, 0, padding.top + chartH);
        gradOut.addColorStop(0, 'rgba(244, 63, 94, 0.25)');
        gradOut.addColorStop(0.6, 'rgba(244, 63, 94, 0.06)');
        gradOut.addColorStop(1, 'rgba(244, 63, 94, 0.00)');
        ctx.fillStyle = gradOut;
        ctx.fill();
        ctx.restore();

        ctx.save();
        ctx.shadowColor = neonOutGlow;
        ctx.shadowBlur = 10;
        ctx.strokeStyle = neonOut;
        ctx.lineWidth = 3;
        ctx.beginPath();
        for (let i = 0; i < pointCount; i++) {
            const x = getX(i);
            const y = getY(this.data[i].stockOut);
            if (i === 0) ctx.moveTo(x, y);
            else ctx.lineTo(x, y);
        }
        ctx.stroke();
        ctx.restore();

        // Draw Node Points & X Date Labels
        ctx.textAlign = 'center';
        ctx.fillStyle = isDark ? '#94a3b8' : '#64748b';
        ctx.font = '11px "JetBrains Mono", monospace';

        for (let i = 0; i < pointCount; i++) {
            const x = getX(i);
            const d = this.data[i];
            const isHovered = (this.hoverIndex === i);

            // Date label
            let dateLabel = d.date || '';
            if (dateLabel.length >= 10) dateLabel = dateLabel.substring(5);
            if (pointCount <= 10 || i % 2 === 0 || i === pointCount - 1) {
                ctx.fillText(dateLabel, x, padding.top + chartH + 22);
            }

            // Node In
            const yIn = getY(d.stockIn);
            ctx.save();
            ctx.fillStyle = isDark ? '#090d16' : '#ffffff';
            ctx.strokeStyle = neonIn;
            ctx.lineWidth = isHovered ? 3 : 2;
            ctx.beginPath();
            ctx.arc(x, yIn, isHovered ? 6 : 3.5, 0, Math.PI * 2);
            ctx.fill();
            ctx.stroke();
            ctx.restore();

            // Node Out
            const yOut = getY(d.stockOut);
            ctx.save();
            ctx.fillStyle = isDark ? '#090d16' : '#ffffff';
            ctx.strokeStyle = neonOut;
            ctx.lineWidth = isHovered ? 3 : 2;
            ctx.beginPath();
            ctx.arc(x, yOut, isHovered ? 6 : 3.5, 0, Math.PI * 2);
            ctx.fill();
            ctx.stroke();
            ctx.restore();
        }

        // 3. Draw Hover Crosshair & Futuristic HUD Tooltip
        if (this.hoverIndex >= 0 && this.hoverIndex < pointCount) {
            const hx = getX(this.hoverIndex);
            const hd = this.data[this.hoverIndex];

            // Vertical holographic laser crosshair
            ctx.save();
            ctx.strokeStyle = isDark ? 'rgba(0, 242, 254, 0.4)' : 'rgba(37, 99, 235, 0.3)';
            ctx.setLineDash([4, 4]);
            ctx.lineWidth = 1.5;
            ctx.beginPath();
            ctx.moveTo(hx, padding.top);
            ctx.lineTo(hx, padding.top + chartH);
            ctx.stroke();
            ctx.restore();

            // Tooltip HUD Card
            const tipW = 140;
            const tipH = 68;
            let tipX = hx - tipW / 2;
            if (tipX < padding.left) tipX = padding.left + 5;
            if (tipX + tipW > w - padding.right) tipX = w - padding.right - tipW - 5;
            const tipY = padding.top + 8;

            ctx.save();
            ctx.fillStyle = isDark ? 'rgba(10, 15, 29, 0.94)' : 'rgba(255, 255, 255, 0.95)';
            ctx.strokeStyle = isDark ? 'rgba(0, 242, 254, 0.4)' : 'rgba(37, 99, 235, 0.3)';
            ctx.lineWidth = 1;
            ctx.shadowColor = isDark ? 'rgba(0, 242, 254, 0.3)' : 'rgba(0, 0, 0, 0.1)';
            ctx.shadowBlur = 12;

            // Rounded rect
            ctx.beginPath();
            ctx.roundRect(tipX, tipY, tipW, tipH, 8);
            ctx.fill();
            ctx.stroke();

            // Tooltip text
            ctx.fillStyle = isDark ? '#00f2fe' : '#2563eb';
            ctx.font = '600 11px "JetBrains Mono", monospace';
            ctx.textAlign = 'left';
            ctx.fillText(`DATE: ${hd.date}`, tipX + 10, tipY + 18);

            ctx.fillStyle = neonIn;
            ctx.font = '500 11px "Space Grotesk", Inter, sans-serif';
            ctx.fillText(`+ INTAKE: ${hd.stockIn} units`, tipX + 10, tipY + 36);

            ctx.fillStyle = neonOut;
            ctx.fillText(`- DISPATCH: ${hd.stockOut} units`, tipX + 10, tipY + 54);
            ctx.restore();
        }
    }
}
