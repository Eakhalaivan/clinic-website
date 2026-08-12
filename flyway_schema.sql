--
-- PostgreSQL database dump
--

\restrict 1IItSFnspod9RHNFxefJXKvDb1KYXly8B7PtzAYw7UHd0bU94igH2JcW8A0eb5Z

-- Dumped from database version 15.18 (Homebrew)
-- Dumped by pg_dump version 15.18 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: ambulances; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.ambulances (
    id bigint NOT NULL,
    vehicle_number character varying(50) NOT NULL,
    model character varying(100),
    driver_name character varying(100) NOT NULL,
    driver_phone character varying(30) NOT NULL,
    current_latitude numeric(10,8),
    current_longitude numeric(11,8),
    status character varying(30) DEFAULT 'AVAILABLE'::character varying NOT NULL,
    is_active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.ambulances OWNER TO eakhalaivan;

--
-- Name: ambulances_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.ambulances_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ambulances_id_seq OWNER TO eakhalaivan;

--
-- Name: ambulances_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.ambulances_id_seq OWNED BY public.ambulances.id;


--
-- Name: appointment_slots; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.appointment_slots (
    id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    start_time timestamp with time zone NOT NULL,
    end_time timestamp with time zone NOT NULL,
    is_booked boolean DEFAULT false,
    branch_id bigint NOT NULL,
    version bigint DEFAULT 0
);


ALTER TABLE public.appointment_slots OWNER TO eakhalaivan;

--
-- Name: appointment_slots_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.appointment_slots_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.appointment_slots_id_seq OWNER TO eakhalaivan;

--
-- Name: appointment_slots_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.appointment_slots_id_seq OWNED BY public.appointment_slots.id;


--
-- Name: appointments; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.appointments (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    slot_id bigint NOT NULL,
    status character varying(20) NOT NULL,
    reason_for_visit text,
    notes text,
    branch_id bigint NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_appointments_status CHECK (((status)::text = ANY ((ARRAY['AVAILABLE'::character varying, 'BOOKED'::character varying, 'CONFIRMED'::character varying, 'CHECKED_IN'::character varying, 'WAITING'::character varying, 'IN_CONSULTATION'::character varying, 'COMPLETED'::character varying, 'FOLLOW_UP_SCHEDULED'::character varying, 'CANCELLED'::character varying, 'NO_SHOW'::character varying])::text[])))
);


ALTER TABLE public.appointments OWNER TO eakhalaivan;

--
-- Name: appointments_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.appointments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.appointments_id_seq OWNER TO eakhalaivan;

--
-- Name: appointments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.appointments_id_seq OWNED BY public.appointments.id;


--
-- Name: attendance; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.attendance (
    id bigint NOT NULL,
    employee_id bigint NOT NULL,
    date date NOT NULL,
    check_in timestamp with time zone,
    check_out timestamp with time zone,
    status character varying(20) DEFAULT 'PRESENT'::character varying NOT NULL
);


ALTER TABLE public.attendance OWNER TO eakhalaivan;

--
-- Name: attendance_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.attendance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.attendance_id_seq OWNER TO eakhalaivan;

--
-- Name: attendance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.attendance_id_seq OWNED BY public.attendance.id;


--
-- Name: audit_log; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.audit_log (
    id bigint NOT NULL,
    user_id bigint,
    action character varying(100) NOT NULL,
    entity_type character varying(100) NOT NULL,
    entity_id character varying(100),
    metadata jsonb,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.audit_log OWNER TO eakhalaivan;

--
-- Name: audit_log_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.audit_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.audit_log_id_seq OWNER TO eakhalaivan;

--
-- Name: audit_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.audit_log_id_seq OWNED BY public.audit_log.id;


--
-- Name: audit_logs; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.audit_logs (
    id bigint NOT NULL,
    actor_id bigint,
    actor_email character varying(200),
    action character varying(100) NOT NULL,
    entity_type character varying(100),
    entity_id character varying(100),
    details text,
    ip_address character varying(45),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.audit_logs OWNER TO eakhalaivan;

--
-- Name: audit_logs_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.audit_logs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.audit_logs_id_seq OWNER TO eakhalaivan;

--
-- Name: audit_logs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.audit_logs_id_seq OWNED BY public.audit_logs.id;


--
-- Name: backoffice_po_items; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.backoffice_po_items (
    id bigint NOT NULL,
    po_id bigint NOT NULL,
    stock_item_id bigint,
    item_description character varying(255) NOT NULL,
    quantity_ordered integer NOT NULL,
    unit_price numeric(12,2) NOT NULL,
    quantity_received integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.backoffice_po_items OWNER TO eakhalaivan;

--
-- Name: backoffice_po_items_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.backoffice_po_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.backoffice_po_items_id_seq OWNER TO eakhalaivan;

--
-- Name: backoffice_po_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.backoffice_po_items_id_seq OWNED BY public.backoffice_po_items.id;


--
-- Name: backoffice_purchase_orders; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.backoffice_purchase_orders (
    id bigint NOT NULL,
    supplier_id bigint NOT NULL,
    warehouse_id bigint,
    order_date date DEFAULT CURRENT_DATE NOT NULL,
    expected_delivery date,
    status character varying(30) DEFAULT 'DRAFT'::character varying NOT NULL,
    total_amount numeric(12,2),
    raised_by bigint,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.backoffice_purchase_orders OWNER TO eakhalaivan;

--
-- Name: backoffice_purchase_orders_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.backoffice_purchase_orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.backoffice_purchase_orders_id_seq OWNER TO eakhalaivan;

--
-- Name: backoffice_purchase_orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.backoffice_purchase_orders_id_seq OWNED BY public.backoffice_purchase_orders.id;


--
-- Name: backoffice_suppliers; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.backoffice_suppliers (
    id bigint NOT NULL,
    name character varying(200) NOT NULL,
    contact_person character varying(200),
    phone character varying(30),
    email character varying(255),
    address text,
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.backoffice_suppliers OWNER TO eakhalaivan;

--
-- Name: backoffice_suppliers_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.backoffice_suppliers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.backoffice_suppliers_id_seq OWNER TO eakhalaivan;

--
-- Name: backoffice_suppliers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.backoffice_suppliers_id_seq OWNED BY public.backoffice_suppliers.id;


--
-- Name: branches; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.branches (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    address text NOT NULL,
    city character varying(50) NOT NULL,
    state character varying(50) NOT NULL,
    country character varying(50) NOT NULL,
    postal_code character varying(20) NOT NULL,
    phone_number character varying(20),
    email character varying(100),
    is_active boolean DEFAULT true,
    timezone character varying(50) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.branches OWNER TO eakhalaivan;

--
-- Name: branches_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.branches_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.branches_id_seq OWNER TO eakhalaivan;

--
-- Name: branches_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.branches_id_seq OWNED BY public.branches.id;


--
-- Name: campaigns; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.campaigns (
    id bigint NOT NULL,
    title character varying(200) NOT NULL,
    channel character varying(30) DEFAULT 'EMAIL'::character varying NOT NULL,
    target_audience character varying(100) DEFAULT 'ALL_PATIENTS'::character varying NOT NULL,
    content text NOT NULL,
    status character varying(30) DEFAULT 'DRAFT'::character varying NOT NULL,
    sent_count integer DEFAULT 0 NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    sent_at timestamp with time zone
);


ALTER TABLE public.campaigns OWNER TO eakhalaivan;

--
-- Name: campaigns_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.campaigns_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.campaigns_id_seq OWNER TO eakhalaivan;

--
-- Name: campaigns_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.campaigns_id_seq OWNED BY public.campaigns.id;


--
-- Name: care_pathway_steps; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.care_pathway_steps (
    id bigint NOT NULL,
    pathway_id bigint NOT NULL,
    step_number integer NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    step_type character varying(30) NOT NULL,
    due_offset_days integer DEFAULT 0 NOT NULL,
    status character varying(30) DEFAULT 'PENDING'::character varying NOT NULL,
    completed_at timestamp with time zone,
    completed_by bigint
);


ALTER TABLE public.care_pathway_steps OWNER TO eakhalaivan;

--
-- Name: care_pathway_steps_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.care_pathway_steps_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.care_pathway_steps_id_seq OWNER TO eakhalaivan;

--
-- Name: care_pathway_steps_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.care_pathway_steps_id_seq OWNED BY public.care_pathway_steps.id;


--
-- Name: care_pathway_templates; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.care_pathway_templates (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    indication character varying(255) NOT NULL,
    estimated_duration_days integer DEFAULT 7 NOT NULL,
    steps jsonb DEFAULT '[]'::jsonb NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.care_pathway_templates OWNER TO eakhalaivan;

--
-- Name: care_pathway_templates_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.care_pathway_templates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.care_pathway_templates_id_seq OWNER TO eakhalaivan;

--
-- Name: care_pathway_templates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.care_pathway_templates_id_seq OWNED BY public.care_pathway_templates.id;


--
-- Name: cds_alerts; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.cds_alerts (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    rule_id bigint,
    triggered_by_user_id bigint,
    message text NOT NULL,
    severity character varying(20) DEFAULT 'WARNING'::character varying NOT NULL,
    status character varying(30) DEFAULT 'PENDING'::character varying NOT NULL,
    override_reason text,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    acknowledged_at timestamp with time zone
);


ALTER TABLE public.cds_alerts OWNER TO eakhalaivan;

--
-- Name: cds_alerts_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.cds_alerts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cds_alerts_id_seq OWNER TO eakhalaivan;

--
-- Name: cds_alerts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.cds_alerts_id_seq OWNED BY public.cds_alerts.id;


--
-- Name: cds_rules; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.cds_rules (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    trigger_event character varying(50) NOT NULL,
    conditions jsonb DEFAULT '{}'::jsonb NOT NULL,
    severity character varying(20) DEFAULT 'WARNING'::character varying NOT NULL,
    action_type character varying(30) DEFAULT 'SHOW_ALERT'::character varying NOT NULL,
    is_active boolean DEFAULT true NOT NULL,
    version integer DEFAULT 1 NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.cds_rules OWNER TO eakhalaivan;

--
-- Name: cds_rules_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.cds_rules_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cds_rules_id_seq OWNER TO eakhalaivan;

--
-- Name: cds_rules_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.cds_rules_id_seq OWNED BY public.cds_rules.id;


--
-- Name: clinic_outbox_events; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.clinic_outbox_events (
    id bigint NOT NULL,
    aggregate_type character varying(100) NOT NULL,
    aggregate_id character varying(100) NOT NULL,
    event_type character varying(100) NOT NULL,
    payload text NOT NULL,
    status character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    processed_at timestamp with time zone,
    retry_count integer DEFAULT 0,
    last_error text
);


ALTER TABLE public.clinic_outbox_events OWNER TO eakhalaivan;

--
-- Name: clinic_outbox_events_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.clinic_outbox_events_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.clinic_outbox_events_id_seq OWNER TO eakhalaivan;

--
-- Name: clinic_outbox_events_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.clinic_outbox_events_id_seq OWNED BY public.clinic_outbox_events.id;


--
-- Name: clinical_notes; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.clinical_notes (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    subjective text,
    objective text,
    assessment text,
    plan text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.clinical_notes OWNER TO eakhalaivan;

--
-- Name: clinical_notes_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.clinical_notes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.clinical_notes_id_seq OWNER TO eakhalaivan;

--
-- Name: clinical_notes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.clinical_notes_id_seq OWNED BY public.clinical_notes.id;


--
-- Name: coupons; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.coupons (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    discount_type character varying(20) DEFAULT 'PERCENTAGE'::character varying NOT NULL,
    discount_value numeric(10,2) NOT NULL,
    min_order_amount numeric(10,2) DEFAULT 0.00,
    max_discount numeric(10,2),
    valid_from date NOT NULL,
    valid_to date NOT NULL,
    usage_limit integer DEFAULT 100,
    times_used integer DEFAULT 0 NOT NULL,
    is_active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.coupons OWNER TO eakhalaivan;

--
-- Name: coupons_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.coupons_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.coupons_id_seq OWNER TO eakhalaivan;

--
-- Name: coupons_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.coupons_id_seq OWNED BY public.coupons.id;


--
-- Name: daily_metrics; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.daily_metrics (
    id bigint NOT NULL,
    date date NOT NULL,
    total_appointments integer DEFAULT 0,
    completed_appointments integer DEFAULT 0,
    cancelled_appointments integer DEFAULT 0,
    total_revenue numeric(10,2) DEFAULT 0.00,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.daily_metrics OWNER TO eakhalaivan;

--
-- Name: daily_metrics_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.daily_metrics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.daily_metrics_id_seq OWNER TO eakhalaivan;

--
-- Name: daily_metrics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.daily_metrics_id_seq OWNED BY public.daily_metrics.id;


--
-- Name: doctor_followups; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.doctor_followups (
    id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    patient_id bigint NOT NULL,
    linked_appointment_id bigint,
    follow_up_date date NOT NULL,
    reason character varying(255) NOT NULL,
    status character varying(50) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone
);


ALTER TABLE public.doctor_followups OWNER TO eakhalaivan;

--
-- Name: doctor_followups_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.doctor_followups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.doctor_followups_id_seq OWNER TO eakhalaivan;

--
-- Name: doctor_followups_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.doctor_followups_id_seq OWNED BY public.doctor_followups.id;


--
-- Name: doctor_medicines; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.doctor_medicines (
    id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description text,
    image_url character varying(1024),
    price numeric(10,2) NOT NULL,
    unit character varying(100),
    stock_quantity integer DEFAULT 0 NOT NULL,
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.doctor_medicines OWNER TO eakhalaivan;

--
-- Name: doctor_medicines_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.doctor_medicines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.doctor_medicines_id_seq OWNER TO eakhalaivan;

--
-- Name: doctor_medicines_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.doctor_medicines_id_seq OWNED BY public.doctor_medicines.id;


--
-- Name: doctor_performance; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.doctor_performance (
    id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    date date NOT NULL,
    appointments_completed integer DEFAULT 0,
    appointments_cancelled integer DEFAULT 0,
    revenue_generated numeric(10,2) DEFAULT 0.00,
    rating_average numeric(3,2) DEFAULT 0.00,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.doctor_performance OWNER TO eakhalaivan;

--
-- Name: doctor_performance_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.doctor_performance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.doctor_performance_id_seq OWNER TO eakhalaivan;

--
-- Name: doctor_performance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.doctor_performance_id_seq OWNED BY public.doctor_performance.id;


--
-- Name: doctor_prescription_template_items; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.doctor_prescription_template_items (
    id bigint NOT NULL,
    template_id bigint NOT NULL,
    medication_name character varying(255) NOT NULL,
    type character varying(50),
    strength character varying(50),
    dosage character varying(50),
    frequency character varying(100),
    duration character varying(50),
    timing character varying(50),
    instructions text
);


ALTER TABLE public.doctor_prescription_template_items OWNER TO eakhalaivan;

--
-- Name: doctor_prescription_template_items_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.doctor_prescription_template_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.doctor_prescription_template_items_id_seq OWNER TO eakhalaivan;

--
-- Name: doctor_prescription_template_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.doctor_prescription_template_items_id_seq OWNED BY public.doctor_prescription_template_items.id;


--
-- Name: doctor_prescription_templates; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.doctor_prescription_templates (
    id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    category character varying(100) NOT NULL,
    chief_complaint text,
    diagnosis text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.doctor_prescription_templates OWNER TO eakhalaivan;

--
-- Name: doctor_prescription_templates_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.doctor_prescription_templates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.doctor_prescription_templates_id_seq OWNER TO eakhalaivan;

--
-- Name: doctor_prescription_templates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.doctor_prescription_templates_id_seq OWNED BY public.doctor_prescription_templates.id;


--
-- Name: doctor_profiles; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.doctor_profiles (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    specialty character varying(100) NOT NULL,
    qualifications text NOT NULL,
    experience_years integer,
    consultation_fee numeric(10,2) NOT NULL,
    bio text,
    is_active boolean DEFAULT true,
    branch_id bigint NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.doctor_profiles OWNER TO eakhalaivan;

--
-- Name: doctor_profiles_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.doctor_profiles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.doctor_profiles_id_seq OWNER TO eakhalaivan;

--
-- Name: doctor_profiles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.doctor_profiles_id_seq OWNED BY public.doctor_profiles.id;


--
-- Name: doctor_schedule_overrides; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.doctor_schedule_overrides (
    id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    override_date date NOT NULL,
    is_unavailable boolean DEFAULT true NOT NULL,
    start_time time without time zone,
    end_time time without time zone,
    reason character varying(255),
    branch_id bigint NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.doctor_schedule_overrides OWNER TO eakhalaivan;

--
-- Name: doctor_schedule_overrides_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.doctor_schedule_overrides_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.doctor_schedule_overrides_id_seq OWNER TO eakhalaivan;

--
-- Name: doctor_schedule_overrides_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.doctor_schedule_overrides_id_seq OWNED BY public.doctor_schedule_overrides.id;


--
-- Name: doctor_working_hours; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.doctor_working_hours (
    id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    day_of_week smallint NOT NULL,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL,
    slot_duration_minutes smallint DEFAULT 20 NOT NULL,
    is_active boolean DEFAULT true NOT NULL,
    branch_id bigint NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    updated_at timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT chk_working_hours_valid CHECK ((end_time > start_time))
);


ALTER TABLE public.doctor_working_hours OWNER TO eakhalaivan;

--
-- Name: doctor_working_hours_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.doctor_working_hours_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.doctor_working_hours_id_seq OWNER TO eakhalaivan;

--
-- Name: doctor_working_hours_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.doctor_working_hours_id_seq OWNED BY public.doctor_working_hours.id;


--
-- Name: ecommerce_order_items; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.ecommerce_order_items (
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity integer NOT NULL,
    unit_price numeric(10,2) NOT NULL,
    total_price numeric(10,2) NOT NULL
);


ALTER TABLE public.ecommerce_order_items OWNER TO eakhalaivan;

--
-- Name: ecommerce_order_items_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.ecommerce_order_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ecommerce_order_items_id_seq OWNER TO eakhalaivan;

--
-- Name: ecommerce_order_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.ecommerce_order_items_id_seq OWNED BY public.ecommerce_order_items.id;


--
-- Name: ecommerce_orders; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.ecommerce_orders (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    total_amount numeric(10,2) NOT NULL,
    shipping_address text NOT NULL,
    shipping_city character varying(100) NOT NULL,
    postal_code character varying(20) NOT NULL,
    status character varying(30) DEFAULT 'PENDING'::character varying NOT NULL,
    tracking_number character varying(100),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    shipped_at timestamp with time zone
);


ALTER TABLE public.ecommerce_orders OWNER TO eakhalaivan;

--
-- Name: ecommerce_orders_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.ecommerce_orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ecommerce_orders_id_seq OWNER TO eakhalaivan;

--
-- Name: ecommerce_orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.ecommerce_orders_id_seq OWNED BY public.ecommerce_orders.id;


--
-- Name: ecommerce_products; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.ecommerce_products (
    id bigint NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    category character varying(100) DEFAULT 'WELLNESS'::character varying NOT NULL,
    price numeric(10,2) NOT NULL,
    stock_quantity integer DEFAULT 0 NOT NULL,
    sku character varying(100),
    image_url character varying(500),
    medicine_id bigint,
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.ecommerce_products OWNER TO eakhalaivan;

--
-- Name: ecommerce_products_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.ecommerce_products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ecommerce_products_id_seq OWNER TO eakhalaivan;

--
-- Name: ecommerce_products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.ecommerce_products_id_seq OWNED BY public.ecommerce_products.id;


--
-- Name: emergency_requests; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.emergency_requests (
    id bigint NOT NULL,
    request_number character varying(50) NOT NULL,
    patient_id bigint,
    pickup_address text NOT NULL,
    pickup_latitude numeric(10,8),
    pickup_longitude numeric(11,8),
    emergency_type character varying(100) DEFAULT 'CARDIAC'::character varying NOT NULL,
    priority character varying(20) DEFAULT 'CRITICAL'::character varying NOT NULL,
    status character varying(30) DEFAULT 'REQUESTED'::character varying NOT NULL,
    assigned_ambulance_id bigint,
    requested_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    completed_at timestamp with time zone
);


ALTER TABLE public.emergency_requests OWNER TO eakhalaivan;

--
-- Name: emergency_requests_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.emergency_requests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.emergency_requests_id_seq OWNER TO eakhalaivan;

--
-- Name: emergency_requests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.emergency_requests_id_seq OWNED BY public.emergency_requests.id;


--
-- Name: employees; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.employees (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    department character varying(100) NOT NULL,
    designation character varying(100) NOT NULL,
    employment_type character varying(30) DEFAULT 'FULL_TIME'::character varying NOT NULL,
    date_of_joining date NOT NULL,
    salary numeric(12,2),
    branch_id bigint,
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.employees OWNER TO eakhalaivan;

--
-- Name: employees_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.employees_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.employees_id_seq OWNER TO eakhalaivan;

--
-- Name: employees_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.employees_id_seq OWNED BY public.employees.id;


--
-- Name: expenses; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.expenses (
    id bigint NOT NULL,
    branch_id bigint,
    category character varying(80) NOT NULL,
    description text NOT NULL,
    amount numeric(12,2) NOT NULL,
    incurred_on date NOT NULL,
    recorded_by bigint,
    receipt_url character varying(500),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.expenses OWNER TO eakhalaivan;

--
-- Name: expenses_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.expenses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.expenses_id_seq OWNER TO eakhalaivan;

--
-- Name: expenses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.expenses_id_seq OWNED BY public.expenses.id;


--
-- Name: hr_attendance; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.hr_attendance (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    date date NOT NULL,
    clock_in timestamp without time zone NOT NULL,
    clock_out timestamp without time zone
);


ALTER TABLE public.hr_attendance OWNER TO eakhalaivan;

--
-- Name: hr_attendance_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.hr_attendance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hr_attendance_id_seq OWNER TO eakhalaivan;

--
-- Name: hr_attendance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.hr_attendance_id_seq OWNED BY public.hr_attendance.id;


--
-- Name: imaging_procedures; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.imaging_procedures (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(200) NOT NULL,
    modality character varying(30) NOT NULL,
    body_part character varying(100),
    price numeric(10,2) DEFAULT 0.00 NOT NULL,
    is_active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.imaging_procedures OWNER TO eakhalaivan;

--
-- Name: imaging_procedures_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.imaging_procedures_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.imaging_procedures_id_seq OWNER TO eakhalaivan;

--
-- Name: imaging_procedures_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.imaging_procedures_id_seq OWNED BY public.imaging_procedures.id;


--
-- Name: imaging_requests; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.imaging_requests (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id bigint,
    procedure_id bigint NOT NULL,
    priority character varying(20) DEFAULT 'ROUTINE'::character varying NOT NULL,
    clinical_notes text,
    status character varying(30) DEFAULT 'REQUESTED'::character varying NOT NULL,
    requested_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    scheduled_at timestamp with time zone
);


ALTER TABLE public.imaging_requests OWNER TO eakhalaivan;

--
-- Name: imaging_requests_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.imaging_requests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.imaging_requests_id_seq OWNER TO eakhalaivan;

--
-- Name: imaging_requests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.imaging_requests_id_seq OWNED BY public.imaging_requests.id;


--
-- Name: insurance_claims; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.insurance_claims (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    invoice_id bigint,
    provider_name character varying(200) NOT NULL,
    claim_number character varying(100),
    claimed_amount numeric(12,2) NOT NULL,
    approved_amount numeric(12,2),
    status character varying(30) DEFAULT 'SUBMITTED'::character varying NOT NULL,
    submitted_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    settled_at timestamp with time zone,
    notes text
);


ALTER TABLE public.insurance_claims OWNER TO eakhalaivan;

--
-- Name: insurance_claims_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.insurance_claims_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.insurance_claims_id_seq OWNER TO eakhalaivan;

--
-- Name: insurance_claims_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.insurance_claims_id_seq OWNED BY public.insurance_claims.id;


--
-- Name: insurance_pre_authorizations; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.insurance_pre_authorizations (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    provider_name character varying(200) NOT NULL,
    policy_number character varying(100),
    procedure_name character varying(255) NOT NULL,
    estimated_cost numeric(12,2) NOT NULL,
    approved_amount numeric(12,2),
    status character varying(30) DEFAULT 'SUBMITTED'::character varying NOT NULL,
    denial_reason text,
    submitted_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    adjudicated_at timestamp with time zone
);


ALTER TABLE public.insurance_pre_authorizations OWNER TO eakhalaivan;

--
-- Name: insurance_pre_authorizations_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.insurance_pre_authorizations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.insurance_pre_authorizations_id_seq OWNER TO eakhalaivan;

--
-- Name: insurance_pre_authorizations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.insurance_pre_authorizations_id_seq OWNED BY public.insurance_pre_authorizations.id;


--
-- Name: invoice_items; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.invoice_items (
    id bigint NOT NULL,
    invoice_id bigint NOT NULL,
    description character varying(500) NOT NULL,
    quantity integer DEFAULT 1 NOT NULL,
    unit_price numeric(10,2) NOT NULL,
    total_price numeric(10,2) NOT NULL,
    item_type character varying(50) DEFAULT 'OTHER'::character varying NOT NULL,
    reference_id bigint
);


ALTER TABLE public.invoice_items OWNER TO eakhalaivan;

--
-- Name: invoice_items_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.invoice_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.invoice_items_id_seq OWNER TO eakhalaivan;

--
-- Name: invoice_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.invoice_items_id_seq OWNED BY public.invoice_items.id;


--
-- Name: invoices; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.invoices (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    appointment_id bigint,
    amount numeric(10,2) NOT NULL,
    status character varying(20) NOT NULL,
    description character varying(255) NOT NULL,
    due_date timestamp without time zone NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    invoice_number character varying(50),
    tax_amount numeric(10,2) DEFAULT 0.00 NOT NULL,
    discount_amount numeric(10,2) DEFAULT 0.00 NOT NULL,
    total_amount numeric(10,2) DEFAULT 0.00 NOT NULL,
    payment_method character varying(30),
    paid_at timestamp with time zone,
    branch_id bigint
);


ALTER TABLE public.invoices OWNER TO eakhalaivan;

--
-- Name: invoices_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.invoices_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.invoices_id_seq OWNER TO eakhalaivan;

--
-- Name: invoices_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.invoices_id_seq OWNED BY public.invoices.id;


--
-- Name: lab_processing_details; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.lab_processing_details (
    id bigint NOT NULL,
    request_id bigint NOT NULL,
    assigned_technician_id bigint,
    machine_used character varying(100),
    notes text,
    started_at timestamp with time zone
);


ALTER TABLE public.lab_processing_details OWNER TO eakhalaivan;

--
-- Name: lab_processing_details_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.lab_processing_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lab_processing_details_id_seq OWNER TO eakhalaivan;

--
-- Name: lab_processing_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.lab_processing_details_id_seq OWNED BY public.lab_processing_details.id;


--
-- Name: lab_results; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.lab_results (
    id bigint NOT NULL,
    request_id bigint NOT NULL,
    lab_tech_id bigint NOT NULL,
    result_value text NOT NULL,
    reference_range character varying(255),
    unit character varying(50),
    is_abnormal boolean DEFAULT false,
    entered_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    verified_at timestamp with time zone,
    verified_by bigint,
    report_file_url character varying(255),
    is_draft boolean DEFAULT false,
    is_critical boolean DEFAULT false
);


ALTER TABLE public.lab_results OWNER TO eakhalaivan;

--
-- Name: lab_results_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.lab_results_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lab_results_id_seq OWNER TO eakhalaivan;

--
-- Name: lab_results_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.lab_results_id_seq OWNED BY public.lab_results.id;


--
-- Name: lab_sample_collections; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.lab_sample_collections (
    id bigint NOT NULL,
    request_id bigint NOT NULL,
    sample_type character varying(100),
    collector_name character varying(100),
    remarks text,
    sample_image_url character varying(255),
    collected_at timestamp with time zone
);


ALTER TABLE public.lab_sample_collections OWNER TO eakhalaivan;

--
-- Name: lab_sample_collections_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.lab_sample_collections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lab_sample_collections_id_seq OWNER TO eakhalaivan;

--
-- Name: lab_sample_collections_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.lab_sample_collections_id_seq OWNED BY public.lab_sample_collections.id;


--
-- Name: lab_test_catalog; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.lab_test_catalog (
    id bigint NOT NULL,
    test_name character varying(255) NOT NULL,
    test_code character varying(50) NOT NULL,
    description text,
    price numeric(10,2) NOT NULL,
    is_active boolean DEFAULT true,
    reference_range character varying(255),
    unit character varying(50)
);


ALTER TABLE public.lab_test_catalog OWNER TO eakhalaivan;

--
-- Name: lab_test_catalog_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.lab_test_catalog_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lab_test_catalog_id_seq OWNER TO eakhalaivan;

--
-- Name: lab_test_catalog_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.lab_test_catalog_id_seq OWNED BY public.lab_test_catalog.id;


--
-- Name: lab_test_requests; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.lab_test_requests (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id bigint,
    test_catalog_id bigint NOT NULL,
    status character varying(50) DEFAULT 'REQUESTED'::character varying NOT NULL,
    requested_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    sample_collected_at timestamp with time zone,
    priority character varying(50) DEFAULT 'ROUTINE'::character varying,
    accepted_at timestamp without time zone,
    accepted_by_id bigint,
    sample_barcode_id character varying(50),
    lab_request_number character varying(50),
    scheduled_at timestamp with time zone
);


ALTER TABLE public.lab_test_requests OWNER TO eakhalaivan;

--
-- Name: lab_test_requests_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.lab_test_requests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lab_test_requests_id_seq OWNER TO eakhalaivan;

--
-- Name: lab_test_requests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.lab_test_requests_id_seq OWNED BY public.lab_test_requests.id;


--
-- Name: leave_requests; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.leave_requests (
    id bigint NOT NULL,
    employee_id bigint NOT NULL,
    leave_type character varying(30) DEFAULT 'CASUAL'::character varying NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    reason text,
    status character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    reviewed_by bigint,
    reviewed_at timestamp with time zone,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.leave_requests OWNER TO eakhalaivan;

--
-- Name: leave_requests_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.leave_requests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.leave_requests_id_seq OWNER TO eakhalaivan;

--
-- Name: leave_requests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.leave_requests_id_seq OWNED BY public.leave_requests.id;


--
-- Name: ledger_entries; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.ledger_entries (
    id bigint NOT NULL,
    branch_id bigint,
    entry_date date NOT NULL,
    entry_type character varying(20) NOT NULL,
    category character varying(80) NOT NULL,
    amount numeric(14,2) NOT NULL,
    reference_id character varying(100),
    description text,
    recorded_by bigint,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.ledger_entries OWNER TO eakhalaivan;

--
-- Name: ledger_entries_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.ledger_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ledger_entries_id_seq OWNER TO eakhalaivan;

--
-- Name: ledger_entries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.ledger_entries_id_seq OWNED BY public.ledger_entries.id;


--
-- Name: login_history; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.login_history (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    ip_address character varying(45),
    user_agent text,
    success boolean NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.login_history OWNER TO eakhalaivan;

--
-- Name: login_history_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.login_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.login_history_id_seq OWNER TO eakhalaivan;

--
-- Name: login_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.login_history_id_seq OWNED BY public.login_history.id;


--
-- Name: medical_records; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.medical_records (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    record_type character varying(50) NOT NULL,
    title character varying(255) NOT NULL,
    notes text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.medical_records OWNER TO eakhalaivan;

--
-- Name: medical_records_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.medical_records_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.medical_records_id_seq OWNER TO eakhalaivan;

--
-- Name: medical_records_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.medical_records_id_seq OWNED BY public.medical_records.id;


--
-- Name: medication_administration_records; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.medication_administration_records (
    id bigint NOT NULL,
    patient_id bigint,
    prescription_item_id bigint,
    patient_name character varying(255),
    bed_number character varying(100),
    medication_name character varying(255),
    dosage character varying(100),
    scheduled_time timestamp without time zone,
    administered_at timestamp without time zone,
    status character varying(50) DEFAULT 'DUE'::character varying,
    administered_by_user_id bigint,
    notes text
);


ALTER TABLE public.medication_administration_records OWNER TO eakhalaivan;

--
-- Name: medication_administration_records_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.medication_administration_records_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.medication_administration_records_id_seq OWNER TO eakhalaivan;

--
-- Name: medication_administration_records_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.medication_administration_records_id_seq OWNED BY public.medication_administration_records.id;


--
-- Name: medicine_order_items; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.medicine_order_items (
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    doctor_medicine_id bigint NOT NULL,
    quantity integer NOT NULL,
    unit_price_at_order numeric(10,2) NOT NULL
);


ALTER TABLE public.medicine_order_items OWNER TO eakhalaivan;

--
-- Name: medicine_order_items_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.medicine_order_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.medicine_order_items_id_seq OWNER TO eakhalaivan;

--
-- Name: medicine_order_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.medicine_order_items_id_seq OWNED BY public.medicine_order_items.id;


--
-- Name: medicine_orders; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.medicine_orders (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    status character varying(50) NOT NULL,
    total_amount numeric(10,2) NOT NULL,
    payment_id bigint,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.medicine_orders OWNER TO eakhalaivan;

--
-- Name: medicine_orders_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.medicine_orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.medicine_orders_id_seq OWNER TO eakhalaivan;

--
-- Name: medicine_orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.medicine_orders_id_seq OWNED BY public.medicine_orders.id;


--
-- Name: notifications; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.notifications (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    title character varying(255) NOT NULL,
    body text NOT NULL,
    type character varying(80) NOT NULL,
    reference_id bigint,
    is_read boolean DEFAULT false NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.notifications OWNER TO eakhalaivan;

--
-- Name: notifications_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.notifications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.notifications_id_seq OWNER TO eakhalaivan;

--
-- Name: notifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.notifications_id_seq OWNED BY public.notifications.id;


--
-- Name: nurse_patient_assignment; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.nurse_patient_assignment (
    id bigint NOT NULL,
    nurse_id bigint NOT NULL,
    patient_id bigint NOT NULL,
    assigned_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    status character varying(50) DEFAULT 'ACTIVE'::character varying
);


ALTER TABLE public.nurse_patient_assignment OWNER TO eakhalaivan;

--
-- Name: nurse_patient_assignment_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.nurse_patient_assignment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.nurse_patient_assignment_id_seq OWNER TO eakhalaivan;

--
-- Name: nurse_patient_assignment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.nurse_patient_assignment_id_seq OWNED BY public.nurse_patient_assignment.id;


--
-- Name: nursing_notes; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.nursing_notes (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    nurse_id bigint NOT NULL,
    note text NOT NULL,
    recorded_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.nursing_notes OWNER TO eakhalaivan;

--
-- Name: nursing_notes_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.nursing_notes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.nursing_notes_id_seq OWNER TO eakhalaivan;

--
-- Name: nursing_notes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.nursing_notes_id_seq OWNED BY public.nursing_notes.id;


--
-- Name: operating_hours; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.operating_hours (
    id bigint NOT NULL,
    branch_id bigint NOT NULL,
    day_of_week integer NOT NULL,
    open_time time without time zone NOT NULL,
    close_time time without time zone NOT NULL
);


ALTER TABLE public.operating_hours OWNER TO eakhalaivan;

--
-- Name: operating_hours_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.operating_hours_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.operating_hours_id_seq OWNER TO eakhalaivan;

--
-- Name: operating_hours_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.operating_hours_id_seq OWNED BY public.operating_hours.id;


--
-- Name: order_set_templates; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.order_set_templates (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    category character varying(100) NOT NULL,
    diagnosis_codes jsonb DEFAULT '[]'::jsonb NOT NULL,
    items jsonb DEFAULT '[]'::jsonb NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.order_set_templates OWNER TO eakhalaivan;

--
-- Name: order_set_templates_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.order_set_templates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.order_set_templates_id_seq OWNER TO eakhalaivan;

--
-- Name: order_set_templates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.order_set_templates_id_seq OWNED BY public.order_set_templates.id;


--
-- Name: otp_codes; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.otp_codes (
    id bigint NOT NULL,
    code character varying(10) NOT NULL,
    user_id bigint NOT NULL,
    expiry_date timestamp with time zone NOT NULL,
    used boolean DEFAULT false
);


ALTER TABLE public.otp_codes OWNER TO eakhalaivan;

--
-- Name: otp_codes_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.otp_codes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.otp_codes_id_seq OWNER TO eakhalaivan;

--
-- Name: otp_codes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.otp_codes_id_seq OWNED BY public.otp_codes.id;


--
-- Name: patient_care_pathways; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.patient_care_pathways (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    template_id bigint NOT NULL,
    assigned_by_doctor_id bigint,
    status character varying(30) DEFAULT 'ACTIVE'::character varying NOT NULL,
    start_date date DEFAULT CURRENT_DATE NOT NULL,
    target_end_date date,
    actual_end_date date,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.patient_care_pathways OWNER TO eakhalaivan;

--
-- Name: patient_care_pathways_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.patient_care_pathways_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.patient_care_pathways_id_seq OWNER TO eakhalaivan;

--
-- Name: patient_care_pathways_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.patient_care_pathways_id_seq OWNED BY public.patient_care_pathways.id;


--
-- Name: patient_consents; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.patient_consents (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    form_type character varying(255) NOT NULL,
    signature_data text NOT NULL,
    agreed_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.patient_consents OWNER TO eakhalaivan;

--
-- Name: patient_consents_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.patient_consents_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.patient_consents_id_seq OWNER TO eakhalaivan;

--
-- Name: patient_consents_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.patient_consents_id_seq OWNED BY public.patient_consents.id;


--
-- Name: patient_loyalty; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.patient_loyalty (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    points_balance integer DEFAULT 0 NOT NULL,
    tier character varying(20) DEFAULT 'BRONZE'::character varying NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.patient_loyalty OWNER TO eakhalaivan;

--
-- Name: patient_loyalty_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.patient_loyalty_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.patient_loyalty_id_seq OWNER TO eakhalaivan;

--
-- Name: patient_loyalty_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.patient_loyalty_id_seq OWNED BY public.patient_loyalty.id;


--
-- Name: patient_profiles; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.patient_profiles (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    date_of_birth date,
    gender character varying(10),
    blood_group character varying(5),
    emergency_contact_name character varying(100),
    emergency_contact_phone character varying(20),
    address text,
    medical_history_summary text,
    branch_id bigint NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    allergies jsonb DEFAULT '[]'::jsonb,
    chronic_conditions jsonb DEFAULT '[]'::jsonb,
    past_surgeries jsonb DEFAULT '[]'::jsonb,
    family_history jsonb DEFAULT '[]'::jsonb,
    current_medications jsonb DEFAULT '[]'::jsonb,
    insurance_status character varying(50),
    injury_status character varying(50)
);


ALTER TABLE public.patient_profiles OWNER TO eakhalaivan;

--
-- Name: patient_profiles_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.patient_profiles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.patient_profiles_id_seq OWNER TO eakhalaivan;

--
-- Name: patient_profiles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.patient_profiles_id_seq OWNED BY public.patient_profiles.id;


--
-- Name: payments; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.payments (
    id bigint NOT NULL,
    invoice_id bigint NOT NULL,
    amount numeric(12,2) NOT NULL,
    payment_method character varying(30) DEFAULT 'CASH'::character varying NOT NULL,
    transaction_ref character varying(200),
    paid_by bigint,
    recorded_by bigint,
    paid_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    notes text
);


ALTER TABLE public.payments OWNER TO eakhalaivan;

--
-- Name: payments_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.payments_id_seq OWNER TO eakhalaivan;

--
-- Name: payments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.payments_id_seq OWNED BY public.payments.id;


--
-- Name: permissions; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.permissions (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(255)
);


ALTER TABLE public.permissions OWNER TO eakhalaivan;

--
-- Name: permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.permissions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.permissions_id_seq OWNER TO eakhalaivan;

--
-- Name: permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.permissions_id_seq OWNED BY public.permissions.id;


--
-- Name: prescription_items; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.prescription_items (
    id bigint NOT NULL,
    prescription_id bigint NOT NULL,
    medication_name character varying(255) NOT NULL,
    dosage character varying(100) NOT NULL,
    frequency character varying(100) NOT NULL,
    duration character varying(100) NOT NULL,
    instructions text,
    type character varying(50),
    strength character varying(50),
    timing character varying(50)
);


ALTER TABLE public.prescription_items OWNER TO eakhalaivan;

--
-- Name: prescription_items_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.prescription_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.prescription_items_id_seq OWNER TO eakhalaivan;

--
-- Name: prescription_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.prescription_items_id_seq OWNED BY public.prescription_items.id;


--
-- Name: prescriptions; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.prescriptions (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id bigint NOT NULL,
    appointment_id bigint,
    notes text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    pharmacy_status character varying(50) DEFAULT 'PENDING'::character varying,
    dispensed_at timestamp without time zone,
    dispensed_by character varying(255),
    voided_at timestamp without time zone,
    void_reason character varying(255),
    chief_complaint text,
    diagnosis text,
    symptoms text,
    medical_history text,
    follow_up_date timestamp without time zone,
    assigned_pharmacy_user_id bigint
);


ALTER TABLE public.prescriptions OWNER TO eakhalaivan;

--
-- Name: prescriptions_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.prescriptions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.prescriptions_id_seq OWNER TO eakhalaivan;

--
-- Name: prescriptions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.prescriptions_id_seq OWNED BY public.prescriptions.id;


--
-- Name: queue_tokens; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.queue_tokens (
    id bigint NOT NULL,
    branch_id bigint NOT NULL,
    walk_in_id bigint,
    appointment_id bigint,
    token_number integer NOT NULL,
    generated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    status character varying(50) DEFAULT 'WAITING'::character varying
);


ALTER TABLE public.queue_tokens OWNER TO eakhalaivan;

--
-- Name: queue_tokens_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.queue_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.queue_tokens_id_seq OWNER TO eakhalaivan;

--
-- Name: queue_tokens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.queue_tokens_id_seq OWNED BY public.queue_tokens.id;


--
-- Name: radiology_reports; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.radiology_reports (
    id bigint NOT NULL,
    request_id bigint NOT NULL,
    radiologist_id bigint,
    findings text NOT NULL,
    impression text NOT NULL,
    dicom_study_uid character varying(255),
    dicom_image_url character varying(500),
    status character varying(20) DEFAULT 'DRAFT'::character varying NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    finalized_at timestamp with time zone
);


ALTER TABLE public.radiology_reports OWNER TO eakhalaivan;

--
-- Name: radiology_reports_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.radiology_reports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.radiology_reports_id_seq OWNER TO eakhalaivan;

--
-- Name: radiology_reports_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.radiology_reports_id_seq OWNED BY public.radiology_reports.id;


--
-- Name: referrals; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.referrals (
    id bigint NOT NULL,
    referrer_id bigint NOT NULL,
    referee_email character varying(255) NOT NULL,
    status character varying(30) DEFAULT 'PENDING'::character varying NOT NULL,
    reward_coupon character varying(50),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.referrals OWNER TO eakhalaivan;

--
-- Name: referrals_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.referrals_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.referrals_id_seq OWNER TO eakhalaivan;

--
-- Name: referrals_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.referrals_id_seq OWNED BY public.referrals.id;


--
-- Name: refresh_tokens; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.refresh_tokens (
    id bigint NOT NULL,
    token character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    expiry_date timestamp with time zone NOT NULL,
    revoked boolean DEFAULT false
);


ALTER TABLE public.refresh_tokens OWNER TO eakhalaivan;

--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.refresh_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.refresh_tokens_id_seq OWNER TO eakhalaivan;

--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.refresh_tokens_id_seq OWNED BY public.refresh_tokens.id;


--
-- Name: role_permissions; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.role_permissions (
    role_id bigint NOT NULL,
    permission_id bigint NOT NULL
);


ALTER TABLE public.role_permissions OWNER TO eakhalaivan;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(255),
    login_portal character varying(50)
);


ALTER TABLE public.roles OWNER TO eakhalaivan;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.roles_id_seq OWNER TO eakhalaivan;

--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- Name: staff_assignments; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.staff_assignments (
    id bigint NOT NULL,
    branch_id bigint NOT NULL,
    user_id bigint NOT NULL,
    role character varying(50) NOT NULL,
    is_primary boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.staff_assignments OWNER TO eakhalaivan;

--
-- Name: staff_assignments_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.staff_assignments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.staff_assignments_id_seq OWNER TO eakhalaivan;

--
-- Name: staff_assignments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.staff_assignments_id_seq OWNED BY public.staff_assignments.id;


--
-- Name: stock_items; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.stock_items (
    id bigint NOT NULL,
    warehouse_id bigint NOT NULL,
    item_type character varying(20) DEFAULT 'SUPPLY'::character varying NOT NULL,
    item_name character varying(255) NOT NULL,
    sku character varying(100),
    unit character varying(30) DEFAULT 'PCS'::character varying NOT NULL,
    quantity integer DEFAULT 0 NOT NULL,
    reorder_level integer DEFAULT 10 NOT NULL,
    medicine_batch_id bigint,
    last_updated timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.stock_items OWNER TO eakhalaivan;

--
-- Name: stock_items_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.stock_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.stock_items_id_seq OWNER TO eakhalaivan;

--
-- Name: stock_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.stock_items_id_seq OWNED BY public.stock_items.id;


--
-- Name: stock_transfers; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.stock_transfers (
    id bigint NOT NULL,
    from_warehouse_id bigint,
    to_warehouse_id bigint,
    stock_item_id bigint NOT NULL,
    quantity integer NOT NULL,
    transferred_by bigint,
    transferred_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    notes text
);


ALTER TABLE public.stock_transfers OWNER TO eakhalaivan;

--
-- Name: stock_transfers_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.stock_transfers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.stock_transfers_id_seq OWNER TO eakhalaivan;

--
-- Name: stock_transfers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.stock_transfers_id_seq OWNED BY public.stock_transfers.id;


--
-- Name: subscription_plans; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.subscription_plans (
    id bigint NOT NULL,
    plan_name character varying(100) NOT NULL,
    price_monthly numeric(12,2) NOT NULL,
    price_annually numeric(12,2),
    max_users integer DEFAULT 10 NOT NULL,
    max_branches integer DEFAULT 1 NOT NULL,
    features text,
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.subscription_plans OWNER TO eakhalaivan;

--
-- Name: subscription_plans_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.subscription_plans_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.subscription_plans_id_seq OWNER TO eakhalaivan;

--
-- Name: subscription_plans_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.subscription_plans_id_seq OWNED BY public.subscription_plans.id;


--
-- Name: support_messages; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.support_messages (
    id bigint NOT NULL,
    ticket_id bigint NOT NULL,
    sender_id bigint NOT NULL,
    message text NOT NULL,
    is_agent_response boolean DEFAULT false NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.support_messages OWNER TO eakhalaivan;

--
-- Name: support_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.support_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.support_messages_id_seq OWNER TO eakhalaivan;

--
-- Name: support_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.support_messages_id_seq OWNED BY public.support_messages.id;


--
-- Name: support_tickets; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.support_tickets (
    id bigint NOT NULL,
    ticket_number character varying(50) NOT NULL,
    user_id bigint NOT NULL,
    subject character varying(255) NOT NULL,
    category character varying(50) DEFAULT 'GENERAL'::character varying NOT NULL,
    priority character varying(20) DEFAULT 'MEDIUM'::character varying NOT NULL,
    status character varying(30) DEFAULT 'OPEN'::character varying NOT NULL,
    assigned_agent_id bigint,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.support_tickets OWNER TO eakhalaivan;

--
-- Name: support_tickets_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.support_tickets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.support_tickets_id_seq OWNER TO eakhalaivan;

--
-- Name: support_tickets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.support_tickets_id_seq OWNED BY public.support_tickets.id;


--
-- Name: system_configurations; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.system_configurations (
    id bigint NOT NULL,
    config_key character varying(200) NOT NULL,
    config_val text NOT NULL,
    description text,
    updated_by character varying(100),
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.system_configurations OWNER TO eakhalaivan;

--
-- Name: system_configurations_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.system_configurations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.system_configurations_id_seq OWNER TO eakhalaivan;

--
-- Name: system_configurations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.system_configurations_id_seq OWNED BY public.system_configurations.id;


--
-- Name: user_devices; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.user_devices (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    device_id character varying(255) NOT NULL,
    device_name character varying(255),
    last_seen_at timestamp with time zone,
    trusted boolean DEFAULT false
);


ALTER TABLE public.user_devices OWNER TO eakhalaivan;

--
-- Name: user_devices_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.user_devices_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_devices_id_seq OWNER TO eakhalaivan;

--
-- Name: user_devices_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.user_devices_id_seq OWNED BY public.user_devices.id;


--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.user_roles OWNER TO eakhalaivan;

--
-- Name: user_snapshots; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.user_snapshots (
    user_id bigint NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    email character varying(255)
);


ALTER TABLE public.user_snapshots OWNER TO eakhalaivan;

--
-- Name: users; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    phone_number character varying(20),
    password_hash character varying(255) NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    enabled boolean DEFAULT true,
    branch_id bigint,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    mfa_enabled boolean DEFAULT false,
    failed_login_attempts integer DEFAULT 0,
    locked_until timestamp with time zone
);


ALTER TABLE public.users OWNER TO eakhalaivan;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO eakhalaivan;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: vendor_deliveries; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.vendor_deliveries (
    id bigint NOT NULL,
    po_id bigint NOT NULL,
    vendor_user_id bigint NOT NULL,
    tracking_number character varying(100) NOT NULL,
    carrier character varying(100),
    dispatch_date date DEFAULT CURRENT_DATE NOT NULL,
    estimated_delivery date,
    status character varying(30) DEFAULT 'DISPATCHED'::character varying NOT NULL,
    notes text,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.vendor_deliveries OWNER TO eakhalaivan;

--
-- Name: vendor_deliveries_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.vendor_deliveries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.vendor_deliveries_id_seq OWNER TO eakhalaivan;

--
-- Name: vendor_deliveries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.vendor_deliveries_id_seq OWNED BY public.vendor_deliveries.id;


--
-- Name: vital_signs; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.vital_signs (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    nurse_id bigint NOT NULL,
    temperature numeric(5,2),
    blood_pressure character varying(20),
    heart_rate integer,
    respiratory_rate integer,
    oxygen_saturation numeric(5,2),
    recorded_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    notes text,
    appointment_id bigint
);


ALTER TABLE public.vital_signs OWNER TO eakhalaivan;

--
-- Name: vital_signs_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.vital_signs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.vital_signs_id_seq OWNER TO eakhalaivan;

--
-- Name: vital_signs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.vital_signs_id_seq OWNED BY public.vital_signs.id;


--
-- Name: vitals; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.vitals (
    id bigint NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id bigint,
    height_cm integer,
    weight_kg integer,
    blood_pressure character varying(50),
    pulse_bpm integer,
    recorded_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.vitals OWNER TO eakhalaivan;

--
-- Name: vitals_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.vitals_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.vitals_id_seq OWNER TO eakhalaivan;

--
-- Name: vitals_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.vitals_id_seq OWNED BY public.vitals.id;


--
-- Name: walk_in_registrations; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.walk_in_registrations (
    id bigint NOT NULL,
    branch_id bigint NOT NULL,
    patient_id bigint,
    first_name character varying(100),
    last_name character varying(100),
    phone character varying(20),
    reason_for_visit text,
    registered_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    status character varying(50) DEFAULT 'WAITING'::character varying,
    op_number character varying(50)
);


ALTER TABLE public.walk_in_registrations OWNER TO eakhalaivan;

--
-- Name: walk_in_registrations_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.walk_in_registrations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.walk_in_registrations_id_seq OWNER TO eakhalaivan;

--
-- Name: walk_in_registrations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.walk_in_registrations_id_seq OWNED BY public.walk_in_registrations.id;


--
-- Name: warehouses; Type: TABLE; Schema: public; Owner: eakhalaivan
--

CREATE TABLE public.warehouses (
    id bigint NOT NULL,
    name character varying(200) NOT NULL,
    branch_id bigint,
    location character varying(500),
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.warehouses OWNER TO eakhalaivan;

--
-- Name: warehouses_id_seq; Type: SEQUENCE; Schema: public; Owner: eakhalaivan
--

CREATE SEQUENCE public.warehouses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.warehouses_id_seq OWNER TO eakhalaivan;

--
-- Name: warehouses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eakhalaivan
--

ALTER SEQUENCE public.warehouses_id_seq OWNED BY public.warehouses.id;


--
-- Name: ambulances id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ambulances ALTER COLUMN id SET DEFAULT nextval('public.ambulances_id_seq'::regclass);


--
-- Name: appointment_slots id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointment_slots ALTER COLUMN id SET DEFAULT nextval('public.appointment_slots_id_seq'::regclass);


--
-- Name: appointments id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointments ALTER COLUMN id SET DEFAULT nextval('public.appointments_id_seq'::regclass);


--
-- Name: attendance id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.attendance ALTER COLUMN id SET DEFAULT nextval('public.attendance_id_seq'::regclass);


--
-- Name: audit_log id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.audit_log ALTER COLUMN id SET DEFAULT nextval('public.audit_log_id_seq'::regclass);


--
-- Name: audit_logs id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.audit_logs ALTER COLUMN id SET DEFAULT nextval('public.audit_logs_id_seq'::regclass);


--
-- Name: backoffice_po_items id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_po_items ALTER COLUMN id SET DEFAULT nextval('public.backoffice_po_items_id_seq'::regclass);


--
-- Name: backoffice_purchase_orders id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_purchase_orders ALTER COLUMN id SET DEFAULT nextval('public.backoffice_purchase_orders_id_seq'::regclass);


--
-- Name: backoffice_suppliers id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_suppliers ALTER COLUMN id SET DEFAULT nextval('public.backoffice_suppliers_id_seq'::regclass);


--
-- Name: branches id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.branches ALTER COLUMN id SET DEFAULT nextval('public.branches_id_seq'::regclass);


--
-- Name: campaigns id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.campaigns ALTER COLUMN id SET DEFAULT nextval('public.campaigns_id_seq'::regclass);


--
-- Name: care_pathway_steps id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.care_pathway_steps ALTER COLUMN id SET DEFAULT nextval('public.care_pathway_steps_id_seq'::regclass);


--
-- Name: care_pathway_templates id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.care_pathway_templates ALTER COLUMN id SET DEFAULT nextval('public.care_pathway_templates_id_seq'::regclass);


--
-- Name: cds_alerts id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.cds_alerts ALTER COLUMN id SET DEFAULT nextval('public.cds_alerts_id_seq'::regclass);


--
-- Name: cds_rules id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.cds_rules ALTER COLUMN id SET DEFAULT nextval('public.cds_rules_id_seq'::regclass);


--
-- Name: clinic_outbox_events id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.clinic_outbox_events ALTER COLUMN id SET DEFAULT nextval('public.clinic_outbox_events_id_seq'::regclass);


--
-- Name: clinical_notes id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.clinical_notes ALTER COLUMN id SET DEFAULT nextval('public.clinical_notes_id_seq'::regclass);


--
-- Name: coupons id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.coupons ALTER COLUMN id SET DEFAULT nextval('public.coupons_id_seq'::regclass);


--
-- Name: daily_metrics id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.daily_metrics ALTER COLUMN id SET DEFAULT nextval('public.daily_metrics_id_seq'::regclass);


--
-- Name: doctor_followups id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_followups ALTER COLUMN id SET DEFAULT nextval('public.doctor_followups_id_seq'::regclass);


--
-- Name: doctor_medicines id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_medicines ALTER COLUMN id SET DEFAULT nextval('public.doctor_medicines_id_seq'::regclass);


--
-- Name: doctor_performance id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_performance ALTER COLUMN id SET DEFAULT nextval('public.doctor_performance_id_seq'::regclass);


--
-- Name: doctor_prescription_template_items id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_prescription_template_items ALTER COLUMN id SET DEFAULT nextval('public.doctor_prescription_template_items_id_seq'::regclass);


--
-- Name: doctor_prescription_templates id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_prescription_templates ALTER COLUMN id SET DEFAULT nextval('public.doctor_prescription_templates_id_seq'::regclass);


--
-- Name: doctor_profiles id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_profiles ALTER COLUMN id SET DEFAULT nextval('public.doctor_profiles_id_seq'::regclass);


--
-- Name: doctor_schedule_overrides id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_schedule_overrides ALTER COLUMN id SET DEFAULT nextval('public.doctor_schedule_overrides_id_seq'::regclass);


--
-- Name: doctor_working_hours id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_working_hours ALTER COLUMN id SET DEFAULT nextval('public.doctor_working_hours_id_seq'::regclass);


--
-- Name: ecommerce_order_items id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_order_items ALTER COLUMN id SET DEFAULT nextval('public.ecommerce_order_items_id_seq'::regclass);


--
-- Name: ecommerce_orders id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_orders ALTER COLUMN id SET DEFAULT nextval('public.ecommerce_orders_id_seq'::regclass);


--
-- Name: ecommerce_products id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_products ALTER COLUMN id SET DEFAULT nextval('public.ecommerce_products_id_seq'::regclass);


--
-- Name: emergency_requests id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.emergency_requests ALTER COLUMN id SET DEFAULT nextval('public.emergency_requests_id_seq'::regclass);


--
-- Name: employees id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.employees ALTER COLUMN id SET DEFAULT nextval('public.employees_id_seq'::regclass);


--
-- Name: expenses id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.expenses ALTER COLUMN id SET DEFAULT nextval('public.expenses_id_seq'::regclass);


--
-- Name: hr_attendance id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.hr_attendance ALTER COLUMN id SET DEFAULT nextval('public.hr_attendance_id_seq'::regclass);


--
-- Name: imaging_procedures id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.imaging_procedures ALTER COLUMN id SET DEFAULT nextval('public.imaging_procedures_id_seq'::regclass);


--
-- Name: imaging_requests id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.imaging_requests ALTER COLUMN id SET DEFAULT nextval('public.imaging_requests_id_seq'::regclass);


--
-- Name: insurance_claims id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.insurance_claims ALTER COLUMN id SET DEFAULT nextval('public.insurance_claims_id_seq'::regclass);


--
-- Name: insurance_pre_authorizations id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.insurance_pre_authorizations ALTER COLUMN id SET DEFAULT nextval('public.insurance_pre_authorizations_id_seq'::regclass);


--
-- Name: invoice_items id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.invoice_items ALTER COLUMN id SET DEFAULT nextval('public.invoice_items_id_seq'::regclass);


--
-- Name: invoices id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.invoices ALTER COLUMN id SET DEFAULT nextval('public.invoices_id_seq'::regclass);


--
-- Name: lab_processing_details id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_processing_details ALTER COLUMN id SET DEFAULT nextval('public.lab_processing_details_id_seq'::regclass);


--
-- Name: lab_results id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_results ALTER COLUMN id SET DEFAULT nextval('public.lab_results_id_seq'::regclass);


--
-- Name: lab_sample_collections id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_sample_collections ALTER COLUMN id SET DEFAULT nextval('public.lab_sample_collections_id_seq'::regclass);


--
-- Name: lab_test_catalog id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_catalog ALTER COLUMN id SET DEFAULT nextval('public.lab_test_catalog_id_seq'::regclass);


--
-- Name: lab_test_requests id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_requests ALTER COLUMN id SET DEFAULT nextval('public.lab_test_requests_id_seq'::regclass);


--
-- Name: leave_requests id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.leave_requests ALTER COLUMN id SET DEFAULT nextval('public.leave_requests_id_seq'::regclass);


--
-- Name: ledger_entries id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ledger_entries ALTER COLUMN id SET DEFAULT nextval('public.ledger_entries_id_seq'::regclass);


--
-- Name: login_history id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.login_history ALTER COLUMN id SET DEFAULT nextval('public.login_history_id_seq'::regclass);


--
-- Name: medical_records id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medical_records ALTER COLUMN id SET DEFAULT nextval('public.medical_records_id_seq'::regclass);


--
-- Name: medication_administration_records id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medication_administration_records ALTER COLUMN id SET DEFAULT nextval('public.medication_administration_records_id_seq'::regclass);


--
-- Name: medicine_order_items id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_order_items ALTER COLUMN id SET DEFAULT nextval('public.medicine_order_items_id_seq'::regclass);


--
-- Name: medicine_orders id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_orders ALTER COLUMN id SET DEFAULT nextval('public.medicine_orders_id_seq'::regclass);


--
-- Name: notifications id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.notifications ALTER COLUMN id SET DEFAULT nextval('public.notifications_id_seq'::regclass);


--
-- Name: nurse_patient_assignment id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.nurse_patient_assignment ALTER COLUMN id SET DEFAULT nextval('public.nurse_patient_assignment_id_seq'::regclass);


--
-- Name: nursing_notes id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.nursing_notes ALTER COLUMN id SET DEFAULT nextval('public.nursing_notes_id_seq'::regclass);


--
-- Name: operating_hours id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.operating_hours ALTER COLUMN id SET DEFAULT nextval('public.operating_hours_id_seq'::regclass);


--
-- Name: order_set_templates id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.order_set_templates ALTER COLUMN id SET DEFAULT nextval('public.order_set_templates_id_seq'::regclass);


--
-- Name: otp_codes id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.otp_codes ALTER COLUMN id SET DEFAULT nextval('public.otp_codes_id_seq'::regclass);


--
-- Name: patient_care_pathways id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_care_pathways ALTER COLUMN id SET DEFAULT nextval('public.patient_care_pathways_id_seq'::regclass);


--
-- Name: patient_consents id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_consents ALTER COLUMN id SET DEFAULT nextval('public.patient_consents_id_seq'::regclass);


--
-- Name: patient_loyalty id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_loyalty ALTER COLUMN id SET DEFAULT nextval('public.patient_loyalty_id_seq'::regclass);


--
-- Name: patient_profiles id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_profiles ALTER COLUMN id SET DEFAULT nextval('public.patient_profiles_id_seq'::regclass);


--
-- Name: payments id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.payments ALTER COLUMN id SET DEFAULT nextval('public.payments_id_seq'::regclass);


--
-- Name: permissions id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.permissions ALTER COLUMN id SET DEFAULT nextval('public.permissions_id_seq'::regclass);


--
-- Name: prescription_items id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.prescription_items ALTER COLUMN id SET DEFAULT nextval('public.prescription_items_id_seq'::regclass);


--
-- Name: prescriptions id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.prescriptions ALTER COLUMN id SET DEFAULT nextval('public.prescriptions_id_seq'::regclass);


--
-- Name: queue_tokens id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.queue_tokens ALTER COLUMN id SET DEFAULT nextval('public.queue_tokens_id_seq'::regclass);


--
-- Name: radiology_reports id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.radiology_reports ALTER COLUMN id SET DEFAULT nextval('public.radiology_reports_id_seq'::regclass);


--
-- Name: referrals id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.referrals ALTER COLUMN id SET DEFAULT nextval('public.referrals_id_seq'::regclass);


--
-- Name: refresh_tokens id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.refresh_tokens ALTER COLUMN id SET DEFAULT nextval('public.refresh_tokens_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: staff_assignments id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.staff_assignments ALTER COLUMN id SET DEFAULT nextval('public.staff_assignments_id_seq'::regclass);


--
-- Name: stock_items id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.stock_items ALTER COLUMN id SET DEFAULT nextval('public.stock_items_id_seq'::regclass);


--
-- Name: stock_transfers id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.stock_transfers ALTER COLUMN id SET DEFAULT nextval('public.stock_transfers_id_seq'::regclass);


--
-- Name: subscription_plans id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.subscription_plans ALTER COLUMN id SET DEFAULT nextval('public.subscription_plans_id_seq'::regclass);


--
-- Name: support_messages id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.support_messages ALTER COLUMN id SET DEFAULT nextval('public.support_messages_id_seq'::regclass);


--
-- Name: support_tickets id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.support_tickets ALTER COLUMN id SET DEFAULT nextval('public.support_tickets_id_seq'::regclass);


--
-- Name: system_configurations id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.system_configurations ALTER COLUMN id SET DEFAULT nextval('public.system_configurations_id_seq'::regclass);


--
-- Name: user_devices id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.user_devices ALTER COLUMN id SET DEFAULT nextval('public.user_devices_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: vendor_deliveries id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vendor_deliveries ALTER COLUMN id SET DEFAULT nextval('public.vendor_deliveries_id_seq'::regclass);


--
-- Name: vital_signs id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vital_signs ALTER COLUMN id SET DEFAULT nextval('public.vital_signs_id_seq'::regclass);


--
-- Name: vitals id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vitals ALTER COLUMN id SET DEFAULT nextval('public.vitals_id_seq'::regclass);


--
-- Name: walk_in_registrations id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.walk_in_registrations ALTER COLUMN id SET DEFAULT nextval('public.walk_in_registrations_id_seq'::regclass);


--
-- Name: warehouses id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.warehouses ALTER COLUMN id SET DEFAULT nextval('public.warehouses_id_seq'::regclass);


--
-- Name: ambulances ambulances_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ambulances
    ADD CONSTRAINT ambulances_pkey PRIMARY KEY (id);


--
-- Name: ambulances ambulances_vehicle_number_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ambulances
    ADD CONSTRAINT ambulances_vehicle_number_key UNIQUE (vehicle_number);


--
-- Name: appointment_slots appointment_slots_doctor_id_start_time_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointment_slots
    ADD CONSTRAINT appointment_slots_doctor_id_start_time_key UNIQUE (doctor_id, start_time);


--
-- Name: appointment_slots appointment_slots_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointment_slots
    ADD CONSTRAINT appointment_slots_pkey PRIMARY KEY (id);


--
-- Name: appointments appointments_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_pkey PRIMARY KEY (id);


--
-- Name: appointments appointments_slot_id_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_slot_id_key UNIQUE (slot_id);


--
-- Name: attendance attendance_employee_id_date_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT attendance_employee_id_date_key UNIQUE (employee_id, date);


--
-- Name: attendance attendance_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT attendance_pkey PRIMARY KEY (id);


--
-- Name: audit_log audit_log_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.audit_log
    ADD CONSTRAINT audit_log_pkey PRIMARY KEY (id);


--
-- Name: audit_logs audit_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT audit_logs_pkey PRIMARY KEY (id);


--
-- Name: backoffice_po_items backoffice_po_items_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_po_items
    ADD CONSTRAINT backoffice_po_items_pkey PRIMARY KEY (id);


--
-- Name: backoffice_purchase_orders backoffice_purchase_orders_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_purchase_orders
    ADD CONSTRAINT backoffice_purchase_orders_pkey PRIMARY KEY (id);


--
-- Name: backoffice_suppliers backoffice_suppliers_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_suppliers
    ADD CONSTRAINT backoffice_suppliers_pkey PRIMARY KEY (id);


--
-- Name: branches branches_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.branches
    ADD CONSTRAINT branches_pkey PRIMARY KEY (id);


--
-- Name: campaigns campaigns_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.campaigns
    ADD CONSTRAINT campaigns_pkey PRIMARY KEY (id);


--
-- Name: care_pathway_steps care_pathway_steps_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.care_pathway_steps
    ADD CONSTRAINT care_pathway_steps_pkey PRIMARY KEY (id);


--
-- Name: care_pathway_templates care_pathway_templates_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.care_pathway_templates
    ADD CONSTRAINT care_pathway_templates_pkey PRIMARY KEY (id);


--
-- Name: cds_alerts cds_alerts_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.cds_alerts
    ADD CONSTRAINT cds_alerts_pkey PRIMARY KEY (id);


--
-- Name: cds_rules cds_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.cds_rules
    ADD CONSTRAINT cds_rules_pkey PRIMARY KEY (id);


--
-- Name: clinic_outbox_events clinic_outbox_events_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.clinic_outbox_events
    ADD CONSTRAINT clinic_outbox_events_pkey PRIMARY KEY (id);


--
-- Name: clinical_notes clinical_notes_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.clinical_notes
    ADD CONSTRAINT clinical_notes_pkey PRIMARY KEY (id);


--
-- Name: coupons coupons_code_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.coupons
    ADD CONSTRAINT coupons_code_key UNIQUE (code);


--
-- Name: coupons coupons_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.coupons
    ADD CONSTRAINT coupons_pkey PRIMARY KEY (id);


--
-- Name: daily_metrics daily_metrics_date_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.daily_metrics
    ADD CONSTRAINT daily_metrics_date_key UNIQUE (date);


--
-- Name: daily_metrics daily_metrics_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.daily_metrics
    ADD CONSTRAINT daily_metrics_pkey PRIMARY KEY (id);


--
-- Name: doctor_followups doctor_followups_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_followups
    ADD CONSTRAINT doctor_followups_pkey PRIMARY KEY (id);


--
-- Name: doctor_medicines doctor_medicines_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_medicines
    ADD CONSTRAINT doctor_medicines_pkey PRIMARY KEY (id);


--
-- Name: doctor_performance doctor_performance_doctor_id_date_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_performance
    ADD CONSTRAINT doctor_performance_doctor_id_date_key UNIQUE (doctor_id, date);


--
-- Name: doctor_performance doctor_performance_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_performance
    ADD CONSTRAINT doctor_performance_pkey PRIMARY KEY (id);


--
-- Name: doctor_prescription_template_items doctor_prescription_template_items_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_prescription_template_items
    ADD CONSTRAINT doctor_prescription_template_items_pkey PRIMARY KEY (id);


--
-- Name: doctor_prescription_templates doctor_prescription_templates_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_prescription_templates
    ADD CONSTRAINT doctor_prescription_templates_pkey PRIMARY KEY (id);


--
-- Name: doctor_profiles doctor_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_profiles
    ADD CONSTRAINT doctor_profiles_pkey PRIMARY KEY (id);


--
-- Name: doctor_profiles doctor_profiles_user_id_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_profiles
    ADD CONSTRAINT doctor_profiles_user_id_key UNIQUE (user_id);


--
-- Name: doctor_schedule_overrides doctor_schedule_overrides_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_schedule_overrides
    ADD CONSTRAINT doctor_schedule_overrides_pkey PRIMARY KEY (id);


--
-- Name: doctor_working_hours doctor_working_hours_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_working_hours
    ADD CONSTRAINT doctor_working_hours_pkey PRIMARY KEY (id);


--
-- Name: ecommerce_order_items ecommerce_order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_order_items
    ADD CONSTRAINT ecommerce_order_items_pkey PRIMARY KEY (id);


--
-- Name: ecommerce_orders ecommerce_orders_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_orders
    ADD CONSTRAINT ecommerce_orders_pkey PRIMARY KEY (id);


--
-- Name: ecommerce_products ecommerce_products_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_products
    ADD CONSTRAINT ecommerce_products_pkey PRIMARY KEY (id);


--
-- Name: ecommerce_products ecommerce_products_sku_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_products
    ADD CONSTRAINT ecommerce_products_sku_key UNIQUE (sku);


--
-- Name: emergency_requests emergency_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.emergency_requests
    ADD CONSTRAINT emergency_requests_pkey PRIMARY KEY (id);


--
-- Name: emergency_requests emergency_requests_request_number_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.emergency_requests
    ADD CONSTRAINT emergency_requests_request_number_key UNIQUE (request_number);


--
-- Name: employees employees_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (id);


--
-- Name: employees employees_user_id_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_user_id_key UNIQUE (user_id);


--
-- Name: expenses expenses_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT expenses_pkey PRIMARY KEY (id);


--
-- Name: hr_attendance hr_attendance_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.hr_attendance
    ADD CONSTRAINT hr_attendance_pkey PRIMARY KEY (id);


--
-- Name: imaging_procedures imaging_procedures_code_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.imaging_procedures
    ADD CONSTRAINT imaging_procedures_code_key UNIQUE (code);


--
-- Name: imaging_procedures imaging_procedures_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.imaging_procedures
    ADD CONSTRAINT imaging_procedures_pkey PRIMARY KEY (id);


--
-- Name: imaging_requests imaging_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.imaging_requests
    ADD CONSTRAINT imaging_requests_pkey PRIMARY KEY (id);


--
-- Name: insurance_claims insurance_claims_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.insurance_claims
    ADD CONSTRAINT insurance_claims_pkey PRIMARY KEY (id);


--
-- Name: insurance_pre_authorizations insurance_pre_authorizations_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.insurance_pre_authorizations
    ADD CONSTRAINT insurance_pre_authorizations_pkey PRIMARY KEY (id);


--
-- Name: invoice_items invoice_items_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.invoice_items
    ADD CONSTRAINT invoice_items_pkey PRIMARY KEY (id);


--
-- Name: invoices invoices_invoice_number_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.invoices
    ADD CONSTRAINT invoices_invoice_number_key UNIQUE (invoice_number);


--
-- Name: invoices invoices_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.invoices
    ADD CONSTRAINT invoices_pkey PRIMARY KEY (id);


--
-- Name: lab_processing_details lab_processing_details_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_processing_details
    ADD CONSTRAINT lab_processing_details_pkey PRIMARY KEY (id);


--
-- Name: lab_results lab_results_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_results
    ADD CONSTRAINT lab_results_pkey PRIMARY KEY (id);


--
-- Name: lab_sample_collections lab_sample_collections_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_sample_collections
    ADD CONSTRAINT lab_sample_collections_pkey PRIMARY KEY (id);


--
-- Name: lab_test_catalog lab_test_catalog_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_catalog
    ADD CONSTRAINT lab_test_catalog_pkey PRIMARY KEY (id);


--
-- Name: lab_test_catalog lab_test_catalog_test_code_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_catalog
    ADD CONSTRAINT lab_test_catalog_test_code_key UNIQUE (test_code);


--
-- Name: lab_test_requests lab_test_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_requests
    ADD CONSTRAINT lab_test_requests_pkey PRIMARY KEY (id);


--
-- Name: lab_test_requests lab_test_requests_sample_barcode_id_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_requests
    ADD CONSTRAINT lab_test_requests_sample_barcode_id_key UNIQUE (sample_barcode_id);


--
-- Name: leave_requests leave_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.leave_requests
    ADD CONSTRAINT leave_requests_pkey PRIMARY KEY (id);


--
-- Name: ledger_entries ledger_entries_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ledger_entries
    ADD CONSTRAINT ledger_entries_pkey PRIMARY KEY (id);


--
-- Name: login_history login_history_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.login_history
    ADD CONSTRAINT login_history_pkey PRIMARY KEY (id);


--
-- Name: medical_records medical_records_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medical_records
    ADD CONSTRAINT medical_records_pkey PRIMARY KEY (id);


--
-- Name: medication_administration_records medication_administration_records_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medication_administration_records
    ADD CONSTRAINT medication_administration_records_pkey PRIMARY KEY (id);


--
-- Name: medicine_order_items medicine_order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_order_items
    ADD CONSTRAINT medicine_order_items_pkey PRIMARY KEY (id);


--
-- Name: medicine_orders medicine_orders_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_orders
    ADD CONSTRAINT medicine_orders_pkey PRIMARY KEY (id);


--
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- Name: nurse_patient_assignment nurse_patient_assignment_nurse_id_patient_id_status_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.nurse_patient_assignment
    ADD CONSTRAINT nurse_patient_assignment_nurse_id_patient_id_status_key UNIQUE (nurse_id, patient_id, status);


--
-- Name: nurse_patient_assignment nurse_patient_assignment_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.nurse_patient_assignment
    ADD CONSTRAINT nurse_patient_assignment_pkey PRIMARY KEY (id);


--
-- Name: nursing_notes nursing_notes_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.nursing_notes
    ADD CONSTRAINT nursing_notes_pkey PRIMARY KEY (id);


--
-- Name: operating_hours operating_hours_branch_id_day_of_week_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.operating_hours
    ADD CONSTRAINT operating_hours_branch_id_day_of_week_key UNIQUE (branch_id, day_of_week);


--
-- Name: operating_hours operating_hours_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.operating_hours
    ADD CONSTRAINT operating_hours_pkey PRIMARY KEY (id);


--
-- Name: order_set_templates order_set_templates_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.order_set_templates
    ADD CONSTRAINT order_set_templates_pkey PRIMARY KEY (id);


--
-- Name: otp_codes otp_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.otp_codes
    ADD CONSTRAINT otp_codes_pkey PRIMARY KEY (id);


--
-- Name: patient_care_pathways patient_care_pathways_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_care_pathways
    ADD CONSTRAINT patient_care_pathways_pkey PRIMARY KEY (id);


--
-- Name: patient_consents patient_consents_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_consents
    ADD CONSTRAINT patient_consents_pkey PRIMARY KEY (id);


--
-- Name: patient_loyalty patient_loyalty_patient_id_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_loyalty
    ADD CONSTRAINT patient_loyalty_patient_id_key UNIQUE (patient_id);


--
-- Name: patient_loyalty patient_loyalty_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_loyalty
    ADD CONSTRAINT patient_loyalty_pkey PRIMARY KEY (id);


--
-- Name: patient_profiles patient_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_profiles
    ADD CONSTRAINT patient_profiles_pkey PRIMARY KEY (id);


--
-- Name: patient_profiles patient_profiles_user_id_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_profiles
    ADD CONSTRAINT patient_profiles_user_id_key UNIQUE (user_id);


--
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);


--
-- Name: permissions permissions_name_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_name_key UNIQUE (name);


--
-- Name: permissions permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (id);


--
-- Name: prescription_items prescription_items_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.prescription_items
    ADD CONSTRAINT prescription_items_pkey PRIMARY KEY (id);


--
-- Name: prescriptions prescriptions_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.prescriptions
    ADD CONSTRAINT prescriptions_pkey PRIMARY KEY (id);


--
-- Name: queue_tokens queue_tokens_branch_id_token_number_generated_at_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.queue_tokens
    ADD CONSTRAINT queue_tokens_branch_id_token_number_generated_at_key UNIQUE (branch_id, token_number, generated_at);


--
-- Name: queue_tokens queue_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.queue_tokens
    ADD CONSTRAINT queue_tokens_pkey PRIMARY KEY (id);


--
-- Name: radiology_reports radiology_reports_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.radiology_reports
    ADD CONSTRAINT radiology_reports_pkey PRIMARY KEY (id);


--
-- Name: radiology_reports radiology_reports_request_id_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.radiology_reports
    ADD CONSTRAINT radiology_reports_request_id_key UNIQUE (request_id);


--
-- Name: referrals referrals_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.referrals
    ADD CONSTRAINT referrals_pkey PRIMARY KEY (id);


--
-- Name: refresh_tokens refresh_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_pkey PRIMARY KEY (id);


--
-- Name: refresh_tokens refresh_tokens_token_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_token_key UNIQUE (token);


--
-- Name: role_permissions role_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_pkey PRIMARY KEY (role_id, permission_id);


--
-- Name: roles roles_name_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_name_key UNIQUE (name);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: staff_assignments staff_assignments_branch_id_user_id_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.staff_assignments
    ADD CONSTRAINT staff_assignments_branch_id_user_id_key UNIQUE (branch_id, user_id);


--
-- Name: staff_assignments staff_assignments_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.staff_assignments
    ADD CONSTRAINT staff_assignments_pkey PRIMARY KEY (id);


--
-- Name: stock_items stock_items_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.stock_items
    ADD CONSTRAINT stock_items_pkey PRIMARY KEY (id);


--
-- Name: stock_transfers stock_transfers_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.stock_transfers
    ADD CONSTRAINT stock_transfers_pkey PRIMARY KEY (id);


--
-- Name: subscription_plans subscription_plans_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.subscription_plans
    ADD CONSTRAINT subscription_plans_pkey PRIMARY KEY (id);


--
-- Name: subscription_plans subscription_plans_plan_name_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.subscription_plans
    ADD CONSTRAINT subscription_plans_plan_name_key UNIQUE (plan_name);


--
-- Name: support_messages support_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.support_messages
    ADD CONSTRAINT support_messages_pkey PRIMARY KEY (id);


--
-- Name: support_tickets support_tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.support_tickets
    ADD CONSTRAINT support_tickets_pkey PRIMARY KEY (id);


--
-- Name: support_tickets support_tickets_ticket_number_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.support_tickets
    ADD CONSTRAINT support_tickets_ticket_number_key UNIQUE (ticket_number);


--
-- Name: system_configurations system_configurations_config_key_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.system_configurations
    ADD CONSTRAINT system_configurations_config_key_key UNIQUE (config_key);


--
-- Name: system_configurations system_configurations_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.system_configurations
    ADD CONSTRAINT system_configurations_pkey PRIMARY KEY (id);


--
-- Name: lab_test_requests uk_lab_request_number; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_requests
    ADD CONSTRAINT uk_lab_request_number UNIQUE (lab_request_number);


--
-- Name: lab_processing_details uk_processing_request; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_processing_details
    ADD CONSTRAINT uk_processing_request UNIQUE (request_id);


--
-- Name: lab_sample_collections uk_sample_request; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_sample_collections
    ADD CONSTRAINT uk_sample_request UNIQUE (request_id);


--
-- Name: doctor_working_hours uq_doctor_day; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_working_hours
    ADD CONSTRAINT uq_doctor_day UNIQUE (doctor_id, day_of_week);


--
-- Name: doctor_schedule_overrides uq_doctor_override_date; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_schedule_overrides
    ADD CONSTRAINT uq_doctor_override_date UNIQUE (doctor_id, override_date);


--
-- Name: user_devices user_devices_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.user_devices
    ADD CONSTRAINT user_devices_pkey PRIMARY KEY (id);


--
-- Name: user_devices user_devices_user_id_device_id_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.user_devices
    ADD CONSTRAINT user_devices_user_id_device_id_key UNIQUE (user_id, device_id);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: user_snapshots user_snapshots_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.user_snapshots
    ADD CONSTRAINT user_snapshots_pkey PRIMARY KEY (user_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_phone_number_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_number_key UNIQUE (phone_number);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: vendor_deliveries vendor_deliveries_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vendor_deliveries
    ADD CONSTRAINT vendor_deliveries_pkey PRIMARY KEY (id);


--
-- Name: vital_signs vital_signs_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vital_signs
    ADD CONSTRAINT vital_signs_pkey PRIMARY KEY (id);


--
-- Name: vitals vitals_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vitals
    ADD CONSTRAINT vitals_pkey PRIMARY KEY (id);


--
-- Name: walk_in_registrations walk_in_registrations_op_number_key; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.walk_in_registrations
    ADD CONSTRAINT walk_in_registrations_op_number_key UNIQUE (op_number);


--
-- Name: walk_in_registrations walk_in_registrations_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.walk_in_registrations
    ADD CONSTRAINT walk_in_registrations_pkey PRIMARY KEY (id);


--
-- Name: warehouses warehouses_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.warehouses
    ADD CONSTRAINT warehouses_pkey PRIMARY KEY (id);


--
-- Name: idx_ambulances_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_ambulances_status ON public.ambulances USING btree (status);


--
-- Name: idx_appointment_slots_doctor_start; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_appointment_slots_doctor_start ON public.appointment_slots USING btree (doctor_id, start_time);


--
-- Name: idx_appointments_doctor_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_appointments_doctor_id ON public.appointments USING btree (doctor_id);


--
-- Name: idx_appointments_patient_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_appointments_patient_id ON public.appointments USING btree (patient_id);


--
-- Name: idx_appointments_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_appointments_status ON public.appointments USING btree (status);


--
-- Name: idx_attendance_employee_date; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_attendance_employee_date ON public.attendance USING btree (employee_id, date);


--
-- Name: idx_audit_logs_actor; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_audit_logs_actor ON public.audit_logs USING btree (actor_id);


--
-- Name: idx_audit_logs_created_at; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_audit_logs_created_at ON public.audit_logs USING btree (created_at DESC);


--
-- Name: idx_campaigns_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_campaigns_status ON public.campaigns USING btree (status);


--
-- Name: idx_care_pathway_steps_pathway; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_care_pathway_steps_pathway ON public.care_pathway_steps USING btree (pathway_id);


--
-- Name: idx_care_pathway_steps_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_care_pathway_steps_status ON public.care_pathway_steps USING btree (status);


--
-- Name: idx_cds_alerts_patient; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_cds_alerts_patient ON public.cds_alerts USING btree (patient_id);


--
-- Name: idx_cds_alerts_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_cds_alerts_status ON public.cds_alerts USING btree (status);


--
-- Name: idx_clinic_outbox_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_clinic_outbox_status ON public.clinic_outbox_events USING btree (status);


--
-- Name: idx_coupons_code; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_coupons_code ON public.coupons USING btree (code);


--
-- Name: idx_doc_template; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_doc_template ON public.doctor_prescription_templates USING btree (doctor_id);


--
-- Name: idx_doctor_medicines_doctor_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_doctor_medicines_doctor_id ON public.doctor_medicines USING btree (doctor_id);


--
-- Name: idx_doctor_profiles_branch_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_doctor_profiles_branch_id ON public.doctor_profiles USING btree (branch_id);


--
-- Name: idx_doctor_profiles_is_active; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_doctor_profiles_is_active ON public.doctor_profiles USING btree (is_active);


--
-- Name: idx_ecommerce_orders_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_ecommerce_orders_status ON public.ecommerce_orders USING btree (status);


--
-- Name: idx_ecommerce_orders_user; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_ecommerce_orders_user ON public.ecommerce_orders USING btree (user_id);


--
-- Name: idx_ecommerce_products_category; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_ecommerce_products_category ON public.ecommerce_products USING btree (category);


--
-- Name: idx_emergency_requests_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_emergency_requests_status ON public.emergency_requests USING btree (status);


--
-- Name: idx_expenses_branch_date; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_expenses_branch_date ON public.expenses USING btree (branch_id, incurred_on);


--
-- Name: idx_followup_date; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_followup_date ON public.doctor_followups USING btree (follow_up_date);


--
-- Name: idx_followup_doctor; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_followup_doctor ON public.doctor_followups USING btree (doctor_id);


--
-- Name: idx_followup_patient; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_followup_patient ON public.doctor_followups USING btree (patient_id);


--
-- Name: idx_imaging_requests_patient; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_imaging_requests_patient ON public.imaging_requests USING btree (patient_id);


--
-- Name: idx_imaging_requests_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_imaging_requests_status ON public.imaging_requests USING btree (status);


--
-- Name: idx_insurance_preauth_patient; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_insurance_preauth_patient ON public.insurance_pre_authorizations USING btree (patient_id);


--
-- Name: idx_insurance_preauth_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_insurance_preauth_status ON public.insurance_pre_authorizations USING btree (status);


--
-- Name: idx_insurance_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_insurance_status ON public.insurance_claims USING btree (status);


--
-- Name: idx_invoices_patient_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_invoices_patient_id ON public.invoices USING btree (patient_id);


--
-- Name: idx_invoices_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_invoices_status ON public.invoices USING btree (status);


--
-- Name: idx_lab_results_request_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_lab_results_request_id ON public.lab_results USING btree (request_id);


--
-- Name: idx_lab_test_requests_patient_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_lab_test_requests_patient_id ON public.lab_test_requests USING btree (patient_id);


--
-- Name: idx_lab_test_requests_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_lab_test_requests_status ON public.lab_test_requests USING btree (status);


--
-- Name: idx_leave_requests_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_leave_requests_status ON public.leave_requests USING btree (status);


--
-- Name: idx_ledger_branch_date; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_ledger_branch_date ON public.ledger_entries USING btree (branch_id, entry_date);


--
-- Name: idx_ledger_type_cat; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_ledger_type_cat ON public.ledger_entries USING btree (entry_type, category);


--
-- Name: idx_medical_records_doctor; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_medical_records_doctor ON public.medical_records USING btree (doctor_id);


--
-- Name: idx_medical_records_patient; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_medical_records_patient ON public.medical_records USING btree (patient_id);


--
-- Name: idx_medicine_orders_doctor_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_medicine_orders_doctor_id ON public.medicine_orders USING btree (doctor_id);


--
-- Name: idx_medicine_orders_patient_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_medicine_orders_patient_id ON public.medicine_orders USING btree (patient_id);


--
-- Name: idx_notifications_user_unread; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_notifications_user_unread ON public.notifications USING btree (user_id, is_read);


--
-- Name: idx_otp_codes_user_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_otp_codes_user_id ON public.otp_codes USING btree (user_id);


--
-- Name: idx_patient_care_pathways_doctor; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_patient_care_pathways_doctor ON public.patient_care_pathways USING btree (assigned_by_doctor_id);


--
-- Name: idx_patient_care_pathways_patient; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_patient_care_pathways_patient ON public.patient_care_pathways USING btree (patient_id);


--
-- Name: idx_patient_profiles_branch_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_patient_profiles_branch_id ON public.patient_profiles USING btree (branch_id);


--
-- Name: idx_payments_invoice; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_payments_invoice ON public.payments USING btree (invoice_id);


--
-- Name: idx_prescription_items_prescription_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_prescription_items_prescription_id ON public.prescription_items USING btree (prescription_id);


--
-- Name: idx_prescriptions_doctor_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_prescriptions_doctor_id ON public.prescriptions USING btree (doctor_id);


--
-- Name: idx_prescriptions_patient_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_prescriptions_patient_id ON public.prescriptions USING btree (patient_id);


--
-- Name: idx_refresh_tokens_user_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_refresh_tokens_user_id ON public.refresh_tokens USING btree (user_id);


--
-- Name: idx_stock_items_batch; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_stock_items_batch ON public.stock_items USING btree (medicine_batch_id);


--
-- Name: idx_stock_items_type; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_stock_items_type ON public.stock_items USING btree (item_type);


--
-- Name: idx_stock_items_warehouse; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_stock_items_warehouse ON public.stock_items USING btree (warehouse_id);


--
-- Name: idx_stock_transfers_item; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_stock_transfers_item ON public.stock_transfers USING btree (stock_item_id);


--
-- Name: idx_support_messages_ticket; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_support_messages_ticket ON public.support_messages USING btree (ticket_id);


--
-- Name: idx_support_tickets_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_support_tickets_status ON public.support_tickets USING btree (status);


--
-- Name: idx_support_tickets_user; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_support_tickets_user ON public.support_tickets USING btree (user_id);


--
-- Name: idx_users_branch_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_users_branch_id ON public.users USING btree (branch_id);


--
-- Name: idx_vendor_deliveries_po; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_vendor_deliveries_po ON public.vendor_deliveries USING btree (po_id);


--
-- Name: idx_vendor_deliveries_vendor; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_vendor_deliveries_vendor ON public.vendor_deliveries USING btree (vendor_user_id);


--
-- Name: idx_working_hours_doctor; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_working_hours_doctor ON public.doctor_working_hours USING btree (doctor_id);


--
-- Name: appointment_slots appointment_slots_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointment_slots
    ADD CONSTRAINT appointment_slots_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.doctor_profiles(id);


--
-- Name: appointments appointments_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.doctor_profiles(id);


--
-- Name: appointments appointments_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient_profiles(id);


--
-- Name: appointments appointments_slot_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_slot_id_fkey FOREIGN KEY (slot_id) REFERENCES public.appointment_slots(id);


--
-- Name: attendance attendance_employee_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT attendance_employee_id_fkey FOREIGN KEY (employee_id) REFERENCES public.employees(id) ON DELETE CASCADE;


--
-- Name: audit_log audit_log_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.audit_log
    ADD CONSTRAINT audit_log_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: audit_logs audit_logs_actor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT audit_logs_actor_id_fkey FOREIGN KEY (actor_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: backoffice_po_items backoffice_po_items_po_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_po_items
    ADD CONSTRAINT backoffice_po_items_po_id_fkey FOREIGN KEY (po_id) REFERENCES public.backoffice_purchase_orders(id) ON DELETE CASCADE;


--
-- Name: backoffice_po_items backoffice_po_items_stock_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_po_items
    ADD CONSTRAINT backoffice_po_items_stock_item_id_fkey FOREIGN KEY (stock_item_id) REFERENCES public.stock_items(id) ON DELETE SET NULL;


--
-- Name: backoffice_purchase_orders backoffice_purchase_orders_raised_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_purchase_orders
    ADD CONSTRAINT backoffice_purchase_orders_raised_by_fkey FOREIGN KEY (raised_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: backoffice_purchase_orders backoffice_purchase_orders_supplier_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_purchase_orders
    ADD CONSTRAINT backoffice_purchase_orders_supplier_id_fkey FOREIGN KEY (supplier_id) REFERENCES public.backoffice_suppliers(id) ON DELETE CASCADE;


--
-- Name: backoffice_purchase_orders backoffice_purchase_orders_warehouse_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_purchase_orders
    ADD CONSTRAINT backoffice_purchase_orders_warehouse_id_fkey FOREIGN KEY (warehouse_id) REFERENCES public.warehouses(id) ON DELETE SET NULL;


--
-- Name: care_pathway_steps care_pathway_steps_completed_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.care_pathway_steps
    ADD CONSTRAINT care_pathway_steps_completed_by_fkey FOREIGN KEY (completed_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: care_pathway_steps care_pathway_steps_pathway_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.care_pathway_steps
    ADD CONSTRAINT care_pathway_steps_pathway_id_fkey FOREIGN KEY (pathway_id) REFERENCES public.patient_care_pathways(id) ON DELETE CASCADE;


--
-- Name: cds_alerts cds_alerts_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.cds_alerts
    ADD CONSTRAINT cds_alerts_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: cds_alerts cds_alerts_rule_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.cds_alerts
    ADD CONSTRAINT cds_alerts_rule_id_fkey FOREIGN KEY (rule_id) REFERENCES public.cds_rules(id) ON DELETE SET NULL;


--
-- Name: cds_alerts cds_alerts_triggered_by_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.cds_alerts
    ADD CONSTRAINT cds_alerts_triggered_by_user_id_fkey FOREIGN KEY (triggered_by_user_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: doctor_followups doctor_followups_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_followups
    ADD CONSTRAINT doctor_followups_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.doctor_profiles(user_id);


--
-- Name: doctor_followups doctor_followups_linked_appointment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_followups
    ADD CONSTRAINT doctor_followups_linked_appointment_id_fkey FOREIGN KEY (linked_appointment_id) REFERENCES public.appointments(id);


--
-- Name: doctor_followups doctor_followups_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_followups
    ADD CONSTRAINT doctor_followups_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id);


--
-- Name: doctor_prescription_template_items doctor_prescription_template_items_template_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_prescription_template_items
    ADD CONSTRAINT doctor_prescription_template_items_template_id_fkey FOREIGN KEY (template_id) REFERENCES public.doctor_prescription_templates(id) ON DELETE CASCADE;


--
-- Name: doctor_schedule_overrides doctor_schedule_overrides_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_schedule_overrides
    ADD CONSTRAINT doctor_schedule_overrides_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.doctor_profiles(id) ON DELETE CASCADE;


--
-- Name: doctor_working_hours doctor_working_hours_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_working_hours
    ADD CONSTRAINT doctor_working_hours_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.doctor_profiles(id) ON DELETE CASCADE;


--
-- Name: ecommerce_order_items ecommerce_order_items_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_order_items
    ADD CONSTRAINT ecommerce_order_items_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.ecommerce_orders(id) ON DELETE CASCADE;


--
-- Name: ecommerce_order_items ecommerce_order_items_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_order_items
    ADD CONSTRAINT ecommerce_order_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.ecommerce_products(id) ON DELETE CASCADE;


--
-- Name: ecommerce_orders ecommerce_orders_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ecommerce_orders
    ADD CONSTRAINT ecommerce_orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: emergency_requests emergency_requests_assigned_ambulance_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.emergency_requests
    ADD CONSTRAINT emergency_requests_assigned_ambulance_id_fkey FOREIGN KEY (assigned_ambulance_id) REFERENCES public.ambulances(id) ON DELETE SET NULL;


--
-- Name: emergency_requests emergency_requests_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.emergency_requests
    ADD CONSTRAINT emergency_requests_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: employees employees_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE SET NULL;


--
-- Name: employees employees_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: expenses expenses_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT expenses_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE SET NULL;


--
-- Name: expenses expenses_recorded_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT expenses_recorded_by_fkey FOREIGN KEY (recorded_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: doctor_medicines fk_doctor_medicines_doctor; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_medicines
    ADD CONSTRAINT fk_doctor_medicines_doctor FOREIGN KEY (doctor_id) REFERENCES public.doctor_profiles(id) ON DELETE CASCADE;


--
-- Name: hr_attendance fk_hr_attendance_user; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.hr_attendance
    ADD CONSTRAINT fk_hr_attendance_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: lab_test_requests fk_lab_requests_accepted_by; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_requests
    ADD CONSTRAINT fk_lab_requests_accepted_by FOREIGN KEY (accepted_by_id) REFERENCES public.users(id);


--
-- Name: medicine_orders fk_medicine_orders_doctor; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_orders
    ADD CONSTRAINT fk_medicine_orders_doctor FOREIGN KEY (doctor_id) REFERENCES public.doctor_profiles(id);


--
-- Name: medicine_orders fk_medicine_orders_patient; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_orders
    ADD CONSTRAINT fk_medicine_orders_patient FOREIGN KEY (patient_id) REFERENCES public.patient_profiles(id);


--
-- Name: medicine_orders fk_medicine_orders_payment; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_orders
    ADD CONSTRAINT fk_medicine_orders_payment FOREIGN KEY (payment_id) REFERENCES public.payments(id);


--
-- Name: medicine_order_items fk_order_items_medicine; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_order_items
    ADD CONSTRAINT fk_order_items_medicine FOREIGN KEY (doctor_medicine_id) REFERENCES public.doctor_medicines(id);


--
-- Name: medicine_order_items fk_order_items_order; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_order_items
    ADD CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES public.medicine_orders(id) ON DELETE CASCADE;


--
-- Name: patient_consents fk_patient_consent_user; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_consents
    ADD CONSTRAINT fk_patient_consent_user FOREIGN KEY (patient_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: prescriptions fk_prescriptions_assigned_pharmacy_user; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.prescriptions
    ADD CONSTRAINT fk_prescriptions_assigned_pharmacy_user FOREIGN KEY (assigned_pharmacy_user_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: lab_processing_details fk_processing_request; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_processing_details
    ADD CONSTRAINT fk_processing_request FOREIGN KEY (request_id) REFERENCES public.lab_test_requests(id) ON DELETE CASCADE;


--
-- Name: lab_processing_details fk_processing_tech; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_processing_details
    ADD CONSTRAINT fk_processing_tech FOREIGN KEY (assigned_technician_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: lab_sample_collections fk_sample_request; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_sample_collections
    ADD CONSTRAINT fk_sample_request FOREIGN KEY (request_id) REFERENCES public.lab_test_requests(id) ON DELETE CASCADE;


--
-- Name: vital_signs fk_vital_signs_appointment; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vital_signs
    ADD CONSTRAINT fk_vital_signs_appointment FOREIGN KEY (appointment_id) REFERENCES public.appointments(id);


--
-- Name: imaging_requests imaging_requests_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.imaging_requests
    ADD CONSTRAINT imaging_requests_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.doctor_profiles(id) ON DELETE SET NULL;


--
-- Name: imaging_requests imaging_requests_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.imaging_requests
    ADD CONSTRAINT imaging_requests_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient_profiles(id) ON DELETE CASCADE;


--
-- Name: imaging_requests imaging_requests_procedure_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.imaging_requests
    ADD CONSTRAINT imaging_requests_procedure_id_fkey FOREIGN KEY (procedure_id) REFERENCES public.imaging_procedures(id) ON DELETE CASCADE;


--
-- Name: insurance_claims insurance_claims_invoice_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.insurance_claims
    ADD CONSTRAINT insurance_claims_invoice_id_fkey FOREIGN KEY (invoice_id) REFERENCES public.invoices(id) ON DELETE SET NULL;


--
-- Name: insurance_claims insurance_claims_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.insurance_claims
    ADD CONSTRAINT insurance_claims_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: insurance_pre_authorizations insurance_pre_authorizations_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.insurance_pre_authorizations
    ADD CONSTRAINT insurance_pre_authorizations_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: invoice_items invoice_items_invoice_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.invoice_items
    ADD CONSTRAINT invoice_items_invoice_id_fkey FOREIGN KEY (invoice_id) REFERENCES public.invoices(id) ON DELETE CASCADE;


--
-- Name: invoices invoices_appointment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.invoices
    ADD CONSTRAINT invoices_appointment_id_fkey FOREIGN KEY (appointment_id) REFERENCES public.appointments(id);


--
-- Name: invoices invoices_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.invoices
    ADD CONSTRAINT invoices_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE SET NULL;


--
-- Name: invoices invoices_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.invoices
    ADD CONSTRAINT invoices_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id);


--
-- Name: lab_results lab_results_lab_tech_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_results
    ADD CONSTRAINT lab_results_lab_tech_id_fkey FOREIGN KEY (lab_tech_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: lab_results lab_results_request_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_results
    ADD CONSTRAINT lab_results_request_id_fkey FOREIGN KEY (request_id) REFERENCES public.lab_test_requests(id) ON DELETE CASCADE;


--
-- Name: lab_results lab_results_verified_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_results
    ADD CONSTRAINT lab_results_verified_by_fkey FOREIGN KEY (verified_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: lab_test_requests lab_test_requests_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_requests
    ADD CONSTRAINT lab_test_requests_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.doctor_profiles(id) ON DELETE SET NULL;


--
-- Name: lab_test_requests lab_test_requests_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_requests
    ADD CONSTRAINT lab_test_requests_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient_profiles(id) ON DELETE CASCADE;


--
-- Name: lab_test_requests lab_test_requests_test_catalog_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.lab_test_requests
    ADD CONSTRAINT lab_test_requests_test_catalog_id_fkey FOREIGN KEY (test_catalog_id) REFERENCES public.lab_test_catalog(id) ON DELETE CASCADE;


--
-- Name: leave_requests leave_requests_employee_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.leave_requests
    ADD CONSTRAINT leave_requests_employee_id_fkey FOREIGN KEY (employee_id) REFERENCES public.employees(id) ON DELETE CASCADE;


--
-- Name: leave_requests leave_requests_reviewed_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.leave_requests
    ADD CONSTRAINT leave_requests_reviewed_by_fkey FOREIGN KEY (reviewed_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: ledger_entries ledger_entries_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ledger_entries
    ADD CONSTRAINT ledger_entries_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE SET NULL;


--
-- Name: ledger_entries ledger_entries_recorded_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ledger_entries
    ADD CONSTRAINT ledger_entries_recorded_by_fkey FOREIGN KEY (recorded_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: login_history login_history_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.login_history
    ADD CONSTRAINT login_history_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: medical_records medical_records_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medical_records
    ADD CONSTRAINT medical_records_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.users(id);


--
-- Name: medical_records medical_records_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medical_records
    ADD CONSTRAINT medical_records_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id);


--
-- Name: notifications notifications_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: nurse_patient_assignment nurse_patient_assignment_nurse_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.nurse_patient_assignment
    ADD CONSTRAINT nurse_patient_assignment_nurse_id_fkey FOREIGN KEY (nurse_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: nurse_patient_assignment nurse_patient_assignment_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.nurse_patient_assignment
    ADD CONSTRAINT nurse_patient_assignment_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient_profiles(id) ON DELETE CASCADE;


--
-- Name: nursing_notes nursing_notes_nurse_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.nursing_notes
    ADD CONSTRAINT nursing_notes_nurse_id_fkey FOREIGN KEY (nurse_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: nursing_notes nursing_notes_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.nursing_notes
    ADD CONSTRAINT nursing_notes_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient_profiles(id) ON DELETE CASCADE;


--
-- Name: operating_hours operating_hours_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.operating_hours
    ADD CONSTRAINT operating_hours_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE CASCADE;


--
-- Name: otp_codes otp_codes_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.otp_codes
    ADD CONSTRAINT otp_codes_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: patient_care_pathways patient_care_pathways_assigned_by_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_care_pathways
    ADD CONSTRAINT patient_care_pathways_assigned_by_doctor_id_fkey FOREIGN KEY (assigned_by_doctor_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: patient_care_pathways patient_care_pathways_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_care_pathways
    ADD CONSTRAINT patient_care_pathways_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: patient_care_pathways patient_care_pathways_template_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_care_pathways
    ADD CONSTRAINT patient_care_pathways_template_id_fkey FOREIGN KEY (template_id) REFERENCES public.care_pathway_templates(id) ON DELETE CASCADE;


--
-- Name: patient_loyalty patient_loyalty_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_loyalty
    ADD CONSTRAINT patient_loyalty_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: payments payments_invoice_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_invoice_id_fkey FOREIGN KEY (invoice_id) REFERENCES public.invoices(id) ON DELETE CASCADE;


--
-- Name: payments payments_paid_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_paid_by_fkey FOREIGN KEY (paid_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: payments payments_recorded_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_recorded_by_fkey FOREIGN KEY (recorded_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: prescription_items prescription_items_prescription_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.prescription_items
    ADD CONSTRAINT prescription_items_prescription_id_fkey FOREIGN KEY (prescription_id) REFERENCES public.prescriptions(id) ON DELETE CASCADE;


--
-- Name: prescriptions prescriptions_appointment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.prescriptions
    ADD CONSTRAINT prescriptions_appointment_id_fkey FOREIGN KEY (appointment_id) REFERENCES public.appointments(id);


--
-- Name: prescriptions prescriptions_doctor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.prescriptions
    ADD CONSTRAINT prescriptions_doctor_id_fkey FOREIGN KEY (doctor_id) REFERENCES public.users(id);


--
-- Name: prescriptions prescriptions_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.prescriptions
    ADD CONSTRAINT prescriptions_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.users(id);


--
-- Name: queue_tokens queue_tokens_appointment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.queue_tokens
    ADD CONSTRAINT queue_tokens_appointment_id_fkey FOREIGN KEY (appointment_id) REFERENCES public.appointments(id) ON DELETE CASCADE;


--
-- Name: queue_tokens queue_tokens_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.queue_tokens
    ADD CONSTRAINT queue_tokens_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE CASCADE;


--
-- Name: queue_tokens queue_tokens_walk_in_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.queue_tokens
    ADD CONSTRAINT queue_tokens_walk_in_id_fkey FOREIGN KEY (walk_in_id) REFERENCES public.walk_in_registrations(id) ON DELETE CASCADE;


--
-- Name: radiology_reports radiology_reports_radiologist_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.radiology_reports
    ADD CONSTRAINT radiology_reports_radiologist_id_fkey FOREIGN KEY (radiologist_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: radiology_reports radiology_reports_request_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.radiology_reports
    ADD CONSTRAINT radiology_reports_request_id_fkey FOREIGN KEY (request_id) REFERENCES public.imaging_requests(id) ON DELETE CASCADE;


--
-- Name: referrals referrals_referrer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.referrals
    ADD CONSTRAINT referrals_referrer_id_fkey FOREIGN KEY (referrer_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: refresh_tokens refresh_tokens_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: role_permissions role_permissions_permission_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_permission_id_fkey FOREIGN KEY (permission_id) REFERENCES public.permissions(id) ON DELETE CASCADE;


--
-- Name: role_permissions role_permissions_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;


--
-- Name: staff_assignments staff_assignments_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.staff_assignments
    ADD CONSTRAINT staff_assignments_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE CASCADE;


--
-- Name: stock_items stock_items_warehouse_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.stock_items
    ADD CONSTRAINT stock_items_warehouse_id_fkey FOREIGN KEY (warehouse_id) REFERENCES public.warehouses(id) ON DELETE CASCADE;


--
-- Name: stock_transfers stock_transfers_from_warehouse_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.stock_transfers
    ADD CONSTRAINT stock_transfers_from_warehouse_id_fkey FOREIGN KEY (from_warehouse_id) REFERENCES public.warehouses(id) ON DELETE SET NULL;


--
-- Name: stock_transfers stock_transfers_stock_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.stock_transfers
    ADD CONSTRAINT stock_transfers_stock_item_id_fkey FOREIGN KEY (stock_item_id) REFERENCES public.stock_items(id) ON DELETE CASCADE;


--
-- Name: stock_transfers stock_transfers_to_warehouse_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.stock_transfers
    ADD CONSTRAINT stock_transfers_to_warehouse_id_fkey FOREIGN KEY (to_warehouse_id) REFERENCES public.warehouses(id) ON DELETE SET NULL;


--
-- Name: stock_transfers stock_transfers_transferred_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.stock_transfers
    ADD CONSTRAINT stock_transfers_transferred_by_fkey FOREIGN KEY (transferred_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: support_messages support_messages_sender_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.support_messages
    ADD CONSTRAINT support_messages_sender_id_fkey FOREIGN KEY (sender_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: support_messages support_messages_ticket_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.support_messages
    ADD CONSTRAINT support_messages_ticket_id_fkey FOREIGN KEY (ticket_id) REFERENCES public.support_tickets(id) ON DELETE CASCADE;


--
-- Name: support_tickets support_tickets_assigned_agent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.support_tickets
    ADD CONSTRAINT support_tickets_assigned_agent_id_fkey FOREIGN KEY (assigned_agent_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: support_tickets support_tickets_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.support_tickets
    ADD CONSTRAINT support_tickets_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_devices user_devices_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.user_devices
    ADD CONSTRAINT user_devices_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: vendor_deliveries vendor_deliveries_po_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vendor_deliveries
    ADD CONSTRAINT vendor_deliveries_po_id_fkey FOREIGN KEY (po_id) REFERENCES public.backoffice_purchase_orders(id) ON DELETE CASCADE;


--
-- Name: vendor_deliveries vendor_deliveries_vendor_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vendor_deliveries
    ADD CONSTRAINT vendor_deliveries_vendor_user_id_fkey FOREIGN KEY (vendor_user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: vital_signs vital_signs_nurse_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vital_signs
    ADD CONSTRAINT vital_signs_nurse_id_fkey FOREIGN KEY (nurse_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: vital_signs vital_signs_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vital_signs
    ADD CONSTRAINT vital_signs_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient_profiles(id) ON DELETE CASCADE;


--
-- Name: vitals vitals_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.vitals
    ADD CONSTRAINT vitals_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient_profiles(id);


--
-- Name: walk_in_registrations walk_in_registrations_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.walk_in_registrations
    ADD CONSTRAINT walk_in_registrations_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE CASCADE;


--
-- Name: walk_in_registrations walk_in_registrations_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.walk_in_registrations
    ADD CONSTRAINT walk_in_registrations_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient_profiles(id) ON DELETE SET NULL;


--
-- Name: warehouses warehouses_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.warehouses
    ADD CONSTRAINT warehouses_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branches(id) ON DELETE SET NULL;


--
-- PostgreSQL database dump complete
--

\unrestrict 1IItSFnspod9RHNFxefJXKvDb1KYXly8B7PtzAYw7UHd0bU94igH2JcW8A0eb5Z

