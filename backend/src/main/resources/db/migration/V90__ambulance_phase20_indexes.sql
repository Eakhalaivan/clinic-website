-- Create indexes for spatial queries and fast lookups in Ambulance module

-- Index on ambulance current location (latitude, longitude) for faster proximity queries
CREATE INDEX idx_ambulance_location ON ambulances (current_latitude, current_longitude);

-- Index on ambulance status to quickly filter out unavailable units
CREATE INDEX idx_ambulance_status ON ambulances (status);

-- Composite index for proximity searches (status + location)
CREATE INDEX idx_ambulance_status_location ON ambulances (status, current_latitude, current_longitude);

-- Indexes for emergency requests
CREATE INDEX idx_emergency_req_status ON emergency_requests (status);
CREATE INDEX idx_emergency_req_priority ON emergency_requests (priority);

-- Indexes for ambulance assignments
CREATE INDEX idx_amb_assignment_status ON ambulance_assignments (status);
CREATE INDEX idx_amb_assignment_ambulance_id ON ambulance_assignments (ambulance_id);
CREATE INDEX idx_amb_assignment_request_id ON ambulance_assignments (emergency_request_id);
