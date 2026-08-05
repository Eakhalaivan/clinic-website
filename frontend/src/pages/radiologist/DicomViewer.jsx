import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { axiosPrivate } from '../../api/axios';
import { Scan, ZoomIn, ZoomOut, RotateCw, Contrast, Maximize, FileText } from 'lucide-react';

const DicomViewer = () => {
  const [zoom, setZoom] = useState(100);
  // Default studyId for demonstration purposes
  const studyId = "ST-90214";

  const { data: dicomData, isLoading, error } = useQuery({
    queryKey: ['dicomMetadata', studyId],
    queryFn: async () => {
      const res = await axiosPrivate.get(`/api/v1/radiology/dicom/study/${studyId}`);
      return res.data;
    }
  });

  return (
    <div style={{ padding: '20px', height: 'calc(100vh - 100px)', display: 'flex', flexDirection: 'column', background: '#090d16', color: '#f8fafc', borderRadius: '12px' }}>
      {/* Top DICOM toolbar */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', paddingBottom: '12px', borderBottom: '1px solid #1e293b' }}>
        <div>
          <h2 style={{ margin: 0, fontSize: '1.1rem', fontWeight: 700, color: '#38bdf8' }}>PACS / DICOM Image Viewer</h2>
          {isLoading ? (
            <p style={{ margin: 0, fontSize: '0.75rem', color: '#94a3b8' }}>Loading study metadata...</p>
          ) : error ? (
            <p style={{ margin: 0, fontSize: '0.75rem', color: '#ef4444' }}>Error loading metadata</p>
          ) : (
            <p style={{ margin: 0, fontSize: '0.75rem', color: '#94a3b8' }}>
              Study #{dicomData.studyId} · {dicomData.studyDescription} · Patient: {dicomData.patientName}
            </p>
          )}
        </div>

        <div style={{ display: 'flex', gap: '8px', background: '#1e293b', padding: '4px', borderRadius: '8px' }}>
          <button onClick={() => setZoom(z => z + 10)} style={{ background: '#334155', color: '#fff', border: 'none', padding: '6px 10px', borderRadius: '6px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.75rem' }}>
            <ZoomIn size={14} /> Zoom In ({zoom}%)
          </button>
          <button onClick={() => setZoom(z => Math.max(50, z - 10))} style={{ background: '#334155', color: '#fff', border: 'none', padding: '6px 10px', borderRadius: '6px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.75rem' }}>
            <ZoomOut size={14} /> Zoom Out
          </button>
          <button style={{ background: '#334155', color: '#fff', border: 'none', padding: '6px 10px', borderRadius: '6px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '4px', fontSize: '0.75rem' }}>
            <RotateCw size={14} /> Rotate
          </button>
          <button style={{ background: '#38bdf8', color: '#0f172a', border: 'none', padding: '6px 12px', borderRadius: '6px', cursor: 'pointer', fontWeight: 700, fontSize: '0.75rem' }}>
            Save Findings Report
          </button>
        </div>
      </div>

      {/* Main Canvas Viewport Area */}
      <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', position: 'relative', overflow: 'hidden', margin: '16px 0' }}>
        <div style={{
          transform: `scale(${zoom / 100})`, transition: 'transform 0.2s ease',
          width: '420px', height: '420px', borderRadius: '8px', border: '2px dashed #334155',
          background: '#020617', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'
        }}>
          <Scan size={64} color="#38bdf8" style={{ opacity: 0.6, marginBottom: '12px' }} />
          <span style={{ fontSize: '0.85rem', color: '#94a3b8', fontWeight: 600 }}>
            {isLoading ? 'Fetching WADO-RS Data...' : '[ Cornerstone.js / OHIF Viewer Canvas ]'}
          </span>
          {!isLoading && !error && (
            <span style={{ fontSize: '0.75rem', color: '#64748b', marginTop: '4px', textAlign: 'center', padding: '0 20px' }}>
              Connected to: {dicomData.wadoRsUrl}
            </span>
          )}
        </div>
      </div>
    </div>
  );
};

export default DicomViewer;
