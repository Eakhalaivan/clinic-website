import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { cn } from '../../../utils/pharmacy/cn';

export default function Sidebar({ items = [], themeColor }) {
  const location = useLocation();

  if (!items || items.length === 0) return null;

  return (
    <aside className="w-64 flex-shrink-0 border-r border-gray-200 bg-white hidden lg:flex flex-col h-full overflow-y-auto">
      <div className="p-4 border-b border-gray-100 mb-4 sticky top-0 bg-white z-10">
        <h2 className="text-sm font-bold text-gray-500 uppercase tracking-wider">Navigation</h2>
      </div>
      <nav className="flex-1 px-3 space-y-1 pb-6">
        {items.map((item, index) => {
          const Icon = item.icon;
          const isActive = location.pathname === item.path || 
                           (item.path !== '/' && location.pathname.startsWith(`${item.path}/`)) ||
                           (item.path === '/' && location.pathname.startsWith('/dashboard'));
          
          return (
            <NavLink
              key={index}
              to={item.path}
              className={cn(
                "flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors group",
                isActive 
                  ? "bg-blue-50 text-blue-700"
                  : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
              )}
              style={isActive && themeColor ? { backgroundColor: `${themeColor}15`, color: themeColor } : {}}
            >
              {Icon && <Icon className={cn("w-5 h-5", isActive ? "text-blue-600" : "text-gray-400 group-hover:text-gray-600")} 
                             style={isActive && themeColor ? { color: themeColor } : {}} />}
              <span className="truncate">{item.label || item.name}</span>
            </NavLink>
          );
        })}
      </nav>
    </aside>
  );
}
