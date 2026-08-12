--
-- PostgreSQL database dump
--

\restrict xA8w5EQERzD6Gm8K3Qe1GjpTpE65kqcORs8IGhU4iXiZTcHV9RsSkp36TL3RJlq

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
-- Name: backoffice_po_items id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_po_items ALTER COLUMN id SET DEFAULT nextval('public.backoffice_po_items_id_seq'::regclass);


--
-- Name: clinic_outbox_events id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.clinic_outbox_events ALTER COLUMN id SET DEFAULT nextval('public.clinic_outbox_events_id_seq'::regclass);


--
-- Name: doctor_medicines id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_medicines ALTER COLUMN id SET DEFAULT nextval('public.doctor_medicines_id_seq'::regclass);


--
-- Name: hr_attendance id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.hr_attendance ALTER COLUMN id SET DEFAULT nextval('public.hr_attendance_id_seq'::regclass);


--
-- Name: ledger_entries id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ledger_entries ALTER COLUMN id SET DEFAULT nextval('public.ledger_entries_id_seq'::regclass);


--
-- Name: medicine_order_items id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_order_items ALTER COLUMN id SET DEFAULT nextval('public.medicine_order_items_id_seq'::regclass);


--
-- Name: medicine_orders id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.medicine_orders ALTER COLUMN id SET DEFAULT nextval('public.medicine_orders_id_seq'::regclass);


--
-- Name: patient_consents id; Type: DEFAULT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_consents ALTER COLUMN id SET DEFAULT nextval('public.patient_consents_id_seq'::regclass);


--
-- Name: backoffice_po_items backoffice_po_items_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.backoffice_po_items
    ADD CONSTRAINT backoffice_po_items_pkey PRIMARY KEY (id);


--
-- Name: clinic_outbox_events clinic_outbox_events_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.clinic_outbox_events
    ADD CONSTRAINT clinic_outbox_events_pkey PRIMARY KEY (id);


--
-- Name: doctor_medicines doctor_medicines_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.doctor_medicines
    ADD CONSTRAINT doctor_medicines_pkey PRIMARY KEY (id);


--
-- Name: hr_attendance hr_attendance_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.hr_attendance
    ADD CONSTRAINT hr_attendance_pkey PRIMARY KEY (id);


--
-- Name: ledger_entries ledger_entries_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.ledger_entries
    ADD CONSTRAINT ledger_entries_pkey PRIMARY KEY (id);


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
-- Name: patient_consents patient_consents_pkey; Type: CONSTRAINT; Schema: public; Owner: eakhalaivan
--

ALTER TABLE ONLY public.patient_consents
    ADD CONSTRAINT patient_consents_pkey PRIMARY KEY (id);


--
-- Name: idx_clinic_outbox_status; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_clinic_outbox_status ON public.clinic_outbox_events USING btree (status);


--
-- Name: idx_doctor_medicines_doctor_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_doctor_medicines_doctor_id ON public.doctor_medicines USING btree (doctor_id);


--
-- Name: idx_ledger_branch_date; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_ledger_branch_date ON public.ledger_entries USING btree (branch_id, entry_date);


--
-- Name: idx_ledger_type_cat; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_ledger_type_cat ON public.ledger_entries USING btree (entry_type, category);


--
-- Name: idx_medicine_orders_doctor_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_medicine_orders_doctor_id ON public.medicine_orders USING btree (doctor_id);


--
-- Name: idx_medicine_orders_patient_id; Type: INDEX; Schema: public; Owner: eakhalaivan
--

CREATE INDEX idx_medicine_orders_patient_id ON public.medicine_orders USING btree (patient_id);


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
-- PostgreSQL database dump complete
--

\unrestrict xA8w5EQERzD6Gm8K3Qe1GjpTpE65kqcORs8IGhU4iXiZTcHV9RsSkp36TL3RJlq

