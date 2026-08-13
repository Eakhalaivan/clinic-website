import React, { useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { DollarSign, ArrowLeft, BarChart3, TrendingUp } from 'lucide-react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import Card from '../../components/ui/Card';
import { fadeIn } from '../../components/ui/motion';

const FinanceDailyCash = () => {
  const { data: payments = [], isLoading } = useQuery({
    queryKey: ['finance-payments-summary'],
    queryFn: async () => {
      const res = await axiosPrivate.get('/finance/payments');
      return res.data;
    }
  });

  const dailySummary = useMemo(() => {
    const summary = {};
    payments.forEach(payment => {
      const date = new Date(payment.paidAt).toLocaleDateString();
      if (!summary[date]) {
        summary[date] = { total: 0, count: 0, cash: 0, card: 0, other: 0 };
      }
      summary[date].total += payment.amount;
      summary[date].count += 1;
      
      if (payment.paymentMethod === 'CASH') summary[date].cash += payment.amount;
      else if (payment.paymentMethod === 'CREDIT_CARD') summary[date].card += payment.amount;
      else summary[date].other += payment.amount;
    });

    // Convert to sorted array
    return Object.entries(summary)
      .map(([date, stats]) => ({ date, ...stats }))
      .sort((a, b) => new Date(b.date) - new Date(a.date));
  }, [payments]);

  const todayStr = new Date().toLocaleDateString();
  const todaysData = dailySummary.find(d => d.date === todayStr) || { total: 0, count: 0, cash: 0, card: 0, other: 0 };

  return (
    <motion.div 
      initial="hidden" 
      animate="visible" 
      variants={fadeIn}
      className="max-w-5xl mx-auto space-y-6"
    >
      <div className="flex items-center justify-between gap-4">
        <div>
          <Link to="/finance" className="inline-flex items-center text-xs font-semibold text-[var(--color-navy-600)] hover:underline mb-2 gap-1">
            <ArrowLeft className="w-3.5 h-3.5" /> Back to Dashboard
          </Link>
          <h1 className="text-2xl sm:text-3xl font-bold font-display text-[var(--color-navy-900)] m-0 flex items-center gap-2">
            <BarChart3 className="w-7 h-7 text-emerald-600" />
            Daily Cash & Summary
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] m-0 mt-1">
            Overview of daily revenue collections and payment breakdowns.
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="bg-emerald-50 border-emerald-100">
          <Card.Body className="p-5">
            <h3 className="text-xs font-bold uppercase tracking-wider text-emerald-800 mb-2">Today's Revenue</h3>
            <p className="text-3xl font-bold text-emerald-600">${todaysData.total.toFixed(2)}</p>
            <p className="text-xs font-semibold text-emerald-700 mt-2">{todaysData.count} transactions today</p>
          </Card.Body>
        </Card>
        <Card>
          <Card.Body className="p-5">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">Cash Collections</h3>
            <p className="text-2xl font-bold text-[var(--color-navy-900)]">${todaysData.cash.toFixed(2)}</p>
            <p className="text-xs font-semibold text-slate-400 mt-2">Today</p>
          </Card.Body>
        </Card>
        <Card>
          <Card.Body className="p-5">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">Card Payments</h3>
            <p className="text-2xl font-bold text-[var(--color-navy-900)]">${todaysData.card.toFixed(2)}</p>
            <p className="text-xs font-semibold text-slate-400 mt-2">Today</p>
          </Card.Body>
        </Card>
        <Card>
          <Card.Body className="p-5">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">Other (Insurance/Bank)</h3>
            <p className="text-2xl font-bold text-[var(--color-navy-900)]">${todaysData.other.toFixed(2)}</p>
            <p className="text-xs font-semibold text-slate-400 mt-2">Today</p>
          </Card.Body>
        </Card>
      </div>

      <Card>
        <Card.Header>
          <h2 className="text-lg font-bold text-[var(--color-navy-900)] flex items-center gap-2">
            <TrendingUp className="w-5 h-5 text-emerald-600" /> Historical Daily Summary
          </h2>
        </Card.Header>
        <Card.Body className="p-0">
          {isLoading ? (
            <div className="p-8 text-center text-sm text-[var(--color-text-muted)]">Loading summaries...</div>
          ) : dailySummary.length === 0 ? (
            <div className="p-8 text-center text-sm text-[var(--color-text-muted)]">No payment history available.</div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-left text-sm">
                <thead className="bg-slate-50 text-slate-500 uppercase text-[10px] font-bold tracking-wider">
                  <tr>
                    <th className="p-4 border-b border-slate-200">Date</th>
                    <th className="p-4 border-b border-slate-200 text-right">Transactions</th>
                    <th className="p-4 border-b border-slate-200 text-right">Cash</th>
                    <th className="p-4 border-b border-slate-200 text-right">Card</th>
                    <th className="p-4 border-b border-slate-200 text-right">Other</th>
                    <th className="p-4 border-b border-slate-200 text-right text-emerald-700 bg-emerald-50/50">Total Revenue</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {dailySummary.map((row, idx) => (
                    <tr key={row.date} className="hover:bg-slate-50 transition-colors">
                      <td className="p-4 font-semibold text-[var(--color-navy-900)]">
                        {row.date === todayStr ? 'Today' : row.date}
                      </td>
                      <td className="p-4 text-right text-slate-500 font-medium">{row.count}</td>
                      <td className="p-4 text-right text-slate-700">${row.cash.toFixed(2)}</td>
                      <td className="p-4 text-right text-slate-700">${row.card.toFixed(2)}</td>
                      <td className="p-4 text-right text-slate-700">${row.other.toFixed(2)}</td>
                      <td className="p-4 text-right font-bold text-emerald-600 bg-emerald-50/30">${row.total.toFixed(2)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </Card.Body>
      </Card>
    </motion.div>
  );
};

export default FinanceDailyCash;
