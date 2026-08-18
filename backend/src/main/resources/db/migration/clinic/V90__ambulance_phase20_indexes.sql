-- Indexes for Ambulance Proximity Search
CREATE INDEX idx_ambulance_location ON ambulances(current_latitude, current_longitude);
CREATE INDEX idx_ambulance_status ON ambulances(status, is_active);

-- Indexes for Assignment Lookups
CREATE INDEX idx_amb_assignment_amb_status ON ambulance_assignments(ambulance_id, status);
CREATE INDEX idx_amb_assignment_request ON ambulance_assignments(request_id);


