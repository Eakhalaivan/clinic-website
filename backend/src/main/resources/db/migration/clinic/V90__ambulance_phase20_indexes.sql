-- Indexes for Ambulance Proximity Search
CREATE INDEX idx_ambulance_location ON ambulance(current_latitude, current_longitude);
CREATE INDEX idx_ambulance_status ON ambulance(status, is_active);

-- Indexes for Assignment Lookups
CREATE INDEX idx_amb_assignment_amb_status ON ambulance_assignment(ambulance_id, status);
CREATE INDEX idx_amb_assignment_request ON ambulance_assignment(request_id);

-- Seed Data for Testing Dispatch
INSERT INTO ambulance (id, license_plate, vehicle_type, status, current_latitude, current_longitude, is_active, last_maintenance_date)
VALUES 
(1, 'AMB-101', 'ALS', 'AVAILABLE', 40.7128, -74.0060, true, CURRENT_DATE),
(2, 'AMB-102', 'BLS', 'AVAILABLE', 40.7300, -73.9950, true, CURRENT_DATE)
ON CONFLICT DO NOTHING;
