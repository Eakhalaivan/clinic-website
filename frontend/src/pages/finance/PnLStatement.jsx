import React from 'react';
import { BarChart3, TrendingUp, TrendingDown } from 'lucide-react';

const PnLStatement = () => {
  return (
    <div style={{ padding: '24px', maxWidth: '900px', margin: '0 auto' }}>
              <h1 style={{ fontSize: '1.5rem', fontWeight: 700, color: '#0f172a', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px' }}>
        <BarChart3 size={24} color="#3f6212" /> Profit & Loss Statement (P&L)
      </h1>

      <div style={{ background: '#fff', borderRadius: '12px', border: '1px solid #e2e8f0', padding: '24px' }}>
        <h3 style={{ margin: '0 0 16px', fontSize: '1rem', fontWeight: 700, color: '#0f172a' }}>Financial Year 2026-Q2 Rollup</h3>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          <div style={{ padding: '12px 16px', background: '#f0fdf4', borderRadius: '8px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontWeight: 600, color: '#166534' }}>Total Revenue (OPD + Pharmacy + Lab)</span>
            <span style={{ fontSize: '1.25rem', fontWeight: 800, color: '#15803d' }}>₹ 14,50,000</span>
          </div>

          <div style={{ padding: '12px 16px', background: '#fef2f2', borderRadius: '8px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontWeight: 600, color: '#991b1b' }}>Total Operating Expenses (Salaries + Stock + Rent)</span>
            <span style={{ fontSize: '1.25rem', fontWeight: 800, color: '#b91c1c' }}>₹ (8,20,000)</span>
          </div>

          <div style={{ height: '1px', background: '#e2e8f0', margin: '8px 0' }} />

          <div style={{ padding: '16px', background: '#eff6ff', borderRadius: '10px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontWeight: 800, fontSize: '1.1rem', color: '#1e40af' }}>Net Operating Profit (EBITDA)</span>
            <span style={{ fontSize: '1.5rem', fontWeight: 900, color: '#1d4ed8' }}>₹ 6,30,000</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PnLStatement;
