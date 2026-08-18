-- V83: Marketing/CRM Phase 16 - Complete Schema
-- Extends existing campaigns, coupons, patient_loyalty, referrals tables
-- and adds all new Marketing/CRM tables.

-- ─── Extend existing campaigns table ────────────────────────────────────────
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS objective       VARCHAR(100);
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS target_segment_id BIGINT;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS channels        JSONB        NOT NULL DEFAULT '["EMAIL"]';
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS content_template_id BIGINT;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS budget          DECIMAL(12,2);
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS owner_id        BIGINT;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS branch_id       BIGINT;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS start_date      TIMESTAMP WITH TIME ZONE;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS end_date        TIMESTAMP WITH TIME ZONE;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS frequency_cap_per_user INT NOT NULL DEFAULT 1;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS approved_by     BIGINT;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS approved_at     TIMESTAMP WITH TIME ZONE;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS scheduled_at    TIMESTAMP WITH TIME ZONE;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS success_metrics JSONB;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS archived_at     TIMESTAMP WITH TIME ZONE;
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();
ALTER TABLE campaigns ADD COLUMN IF NOT EXISTS campaign_type   VARCHAR(50) NOT NULL DEFAULT 'GENERAL';

-- ─── Extend existing coupons table ──────────────────────────────────────────
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS campaign_id         BIGINT;
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS eligible_service_ids JSONB;
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS branch_ids          JSONB;
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS segment_id          BIGINT;
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS per_patient_limit   INT NOT NULL DEFAULT 1;
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS is_stackable        BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS approved_by         BIGINT;
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS approved_at         TIMESTAMP WITH TIME ZONE;
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS purpose             VARCHAR(100);
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();
ALTER TABLE coupons ADD COLUMN IF NOT EXISTS created_by          BIGINT;

-- ─── Extend existing patient_loyalty table ───────────────────────────────────
ALTER TABLE patient_loyalty ADD COLUMN IF NOT EXISTS lifetime_earned    INT NOT NULL DEFAULT 0;
ALTER TABLE patient_loyalty ADD COLUMN IF NOT EXISTS lifetime_redeemed  INT NOT NULL DEFAULT 0;
ALTER TABLE patient_loyalty ADD COLUMN IF NOT EXISTS last_earned_at     TIMESTAMP WITH TIME ZONE;
ALTER TABLE patient_loyalty ADD COLUMN IF NOT EXISTS last_redeemed_at   TIMESTAMP WITH TIME ZONE;

-- ─── Extend existing referrals table ────────────────────────────────────────
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS program_id              BIGINT;
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS referral_code           VARCHAR(30) UNIQUE;
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS referral_link           VARCHAR(500);
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS lead_id                 BIGINT;
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS converted_patient_id    BIGINT;
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS qualifying_reference_id BIGINT;
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS fraud_review_status     VARCHAR(30) NOT NULL DEFAULT 'NOT_REQUIRED';
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS reward_issued_at        TIMESTAMP WITH TIME ZONE;
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS reward_type             VARCHAR(30);
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS reward_value            DECIMAL(10,2);
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS expires_at              TIMESTAMP WITH TIME ZONE;
ALTER TABLE referrals ADD COLUMN IF NOT EXISTS updated_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();

-- ─── Campaign Segments ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS campaign_segments (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    criteria_json   JSONB        NOT NULL DEFAULT '{}',
    estimated_count INT          NOT NULL DEFAULT 0,
    version         INT          NOT NULL DEFAULT 1,
    created_by      BIGINT,
    branch_id       BIGINT,
    is_public       BOOLEAN      NOT NULL DEFAULT FALSE,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- ─── Marketing Consents ───────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS marketing_consents (
    id               BIGSERIAL PRIMARY KEY,
    patient_id       BIGINT,
    lead_id          BIGINT,
    channel          VARCHAR(30)  NOT NULL, -- EMAIL, SMS, WHATSAPP, PUSH, IN_APP
    consent_state    VARCHAR(20)  NOT NULL DEFAULT 'OPTED_IN', -- OPTED_IN, OPTED_OUT
    consent_source   VARCHAR(100), -- REGISTRATION, PORTAL, KIOSK, CAMPAIGN, MANUAL
    wording_version  VARCHAR(20),
    purpose          VARCHAR(100),
    captured_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    expires_at       TIMESTAMP WITH TIME ZONE,
    withdrawn_at     TIMESTAMP WITH TIME ZONE,
    ip_address       VARCHAR(50),
    branch_id        BIGINT,
    operator_id      BIGINT,
    CONSTRAINT chk_consent_owner CHECK (patient_id IS NOT NULL OR lead_id IS NOT NULL)
);
CREATE INDEX IF NOT EXISTS idx_mkt_consent_patient ON marketing_consents(patient_id, channel, consent_state);
CREATE INDEX IF NOT EXISTS idx_mkt_consent_lead ON marketing_consents(lead_id, channel, consent_state);

-- ─── Leads ───────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS leads (
    id                      BIGSERIAL PRIMARY KEY,
    source                  VARCHAR(50)  NOT NULL, -- WEBSITE, KIOSK, WALK_IN, REFERRAL, CAMPAIGN, EVENT, PHONE, PARTNER, MANUAL
    owner_id                BIGINT,
    branch_id               BIGINT,
    first_name              VARCHAR(100),
    last_name               VARCHAR(100),
    phone                   VARCHAR(30),
    email                   VARCHAR(255),
    interest                VARCHAR(200),
    status                  VARCHAR(30)  NOT NULL DEFAULT 'NEW', -- NEW, CONTACTED, QUALIFIED, APPOINTMENT_BOOKED, CONVERTED, NURTURING, LOST, ARCHIVED
    score                   INT          NOT NULL DEFAULT 0,
    deduplication_key       VARCHAR(64)  UNIQUE, -- SHA-256 of normalized phone+email
    campaign_id             BIGINT,
    referral_source         VARCHAR(200),
    communication_preference VARCHAR(30) DEFAULT 'ANY', -- ANY, EMAIL, SMS, PHONE, WHATSAPP
    converted_patient_id    BIGINT,
    lost_reason             TEXT,
    next_action_at          TIMESTAMP WITH TIME ZONE,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_leads_status ON leads(status);
CREATE INDEX IF NOT EXISTS idx_leads_owner ON leads(owner_id);
CREATE INDEX IF NOT EXISTS idx_leads_branch ON leads(branch_id);
CREATE INDEX IF NOT EXISTS idx_leads_dedup ON leads(deduplication_key);

-- ─── Lead Activities ─────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS lead_activities (
    id            BIGSERIAL PRIMARY KEY,
    lead_id       BIGINT       NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    activity_type VARCHAR(50)  NOT NULL, -- CALL, EMAIL, SMS, WHATSAPP, TASK, APPOINTMENT_BOOKED, NOTE, ESCALATION, REASSIGNMENT, LOST
    notes         TEXT,
    outcome       VARCHAR(100),
    next_step     TEXT,
    due_at        TIMESTAMP WITH TIME ZONE,
    completed_at  TIMESTAMP WITH TIME ZONE,
    performed_by  BIGINT,
    channel       VARCHAR(30),
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_lead_activities_lead ON lead_activities(lead_id);

-- ─── Loyalty Tiers ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS loyalty_tiers (
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(50)   NOT NULL UNIQUE,
    min_points          INT           NOT NULL,
    max_points          INT,
    benefits            JSONB,
    earning_multiplier  DECIMAL(4,2)  NOT NULL DEFAULT 1.00,
    branch_scope        JSONB, -- NULL = all branches
    is_active           BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- ─── Loyalty Transactions ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS loyalty_transactions (
    id                BIGSERIAL PRIMARY KEY,
    patient_id        BIGINT       NOT NULL,
    type              VARCHAR(30)  NOT NULL, -- EARNED, REDEEMED, ADJUSTED, EXPIRED, REVERSED, REFUNDED
    points            INT          NOT NULL,
    reference_type    VARCHAR(50), -- INVOICE, REDEMPTION, ADMIN, REFERRAL, EXPIRY
    reference_id      BIGINT,
    balance_before    INT          NOT NULL,
    balance_after     INT          NOT NULL,
    idempotency_key   VARCHAR(100) UNIQUE,
    notes             TEXT,
    approved_by       BIGINT,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_loyalty_tx_patient ON loyalty_transactions(patient_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_loyalty_tx_idem ON loyalty_transactions(idempotency_key);

-- ─── Membership Plans ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS membership_plans (
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(200)  NOT NULL,
    description       TEXT,
    price             DECIMAL(10,2) NOT NULL,
    validity_days     INT           NOT NULL,
    benefits          JSONB,
    included_services JSONB,
    discount_percent  DECIMAL(5,2)  NOT NULL DEFAULT 0,
    max_dependents    INT           NOT NULL DEFAULT 0,
    branch_ids        JSONB, -- NULL = all branches
    renewal_policy    VARCHAR(50),
    status            VARCHAR(30)   NOT NULL DEFAULT 'DRAFT', -- DRAFT, ACTIVE, ARCHIVED
    terms_version     VARCHAR(20),
    created_by        BIGINT,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- ─── Patient Memberships ─────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS patient_memberships (
    id                BIGSERIAL PRIMARY KEY,
    patient_id        BIGINT        NOT NULL,
    plan_id           BIGINT        NOT NULL REFERENCES membership_plans(id),
    status            VARCHAR(30)   NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, EXPIRING, RENEWED, PAUSED, CANCELLED, EXPIRED
    start_date        DATE          NOT NULL,
    end_date          DATE          NOT NULL,
    activated_by      BIGINT,
    cancelled_at      TIMESTAMP WITH TIME ZONE,
    cancel_reason     TEXT,
    usage_summary     JSONB,
    renewed_from_id   BIGINT,
    invoice_id        BIGINT,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_active_membership UNIQUE (patient_id, plan_id, status)
);
CREATE INDEX IF NOT EXISTS idx_patient_memberships_patient ON patient_memberships(patient_id);

-- ─── Referral Programs ───────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS referral_programs (
    id                        BIGSERIAL PRIMARY KEY,
    name                      VARCHAR(200) NOT NULL,
    reward_type               VARCHAR(30)  NOT NULL, -- POINTS, COUPON, GIFT_CARD
    reward_value              DECIMAL(10,2) NOT NULL,
    qualifying_event          VARCHAR(50)  NOT NULL, -- APPOINTMENT_COMPLETED, PAID_INVOICE
    max_reward_per_referrer   INT          NOT NULL DEFAULT 10,
    max_reward_per_referee    INT          NOT NULL DEFAULT 1,
    expiry_days               INT          NOT NULL DEFAULT 90,
    fraud_review_required     BOOLEAN      NOT NULL DEFAULT FALSE,
    status                    VARCHAR(30)  NOT NULL DEFAULT 'ACTIVE',
    created_at                TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- ─── Gift Cards ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS gift_cards (
    id                    BIGSERIAL PRIMARY KEY,
    code_hash             VARCHAR(64)   NOT NULL UNIQUE, -- SHA-256 of plaintext code
    code_suffix           VARCHAR(4)    NOT NULL, -- last 4 chars for display
    initial_balance       DECIMAL(10,2) NOT NULL,
    current_balance       DECIMAL(10,2) NOT NULL,
    issued_to_patient_id  BIGINT,
    purchased_by_patient_id BIGINT,
    purchase_invoice_id   BIGINT,
    branch_id             BIGINT,
    status                VARCHAR(30)   NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, REDEEMED, EXPIRED, CANCELLED
    activated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    expires_at            TIMESTAMP WITH TIME ZONE,
    cancelled_at          TIMESTAMP WITH TIME ZONE,
    redemption_count      INT           NOT NULL DEFAULT 0,
    created_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_gift_cards_patient ON gift_cards(issued_to_patient_id);

-- ─── Coupon Usages ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS coupon_usages (
    id              BIGSERIAL PRIMARY KEY,
    coupon_id       BIGINT        NOT NULL REFERENCES coupons(id),
    patient_id      BIGINT        NOT NULL,
    invoice_id      BIGINT,
    applied_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    discount_applied DECIMAL(10,2) NOT NULL,
    branch_id       BIGINT,
    CONSTRAINT uq_coupon_patient_invoice UNIQUE (coupon_id, patient_id, invoice_id)
);
CREATE INDEX IF NOT EXISTS idx_coupon_usages_coupon ON coupon_usages(coupon_id);
CREATE INDEX IF NOT EXISTS idx_coupon_usages_patient ON coupon_usages(patient_id);

-- ─── Campaign Deliveries ─────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS campaign_deliveries (
    id                  BIGSERIAL PRIMARY KEY,
    campaign_id         BIGINT       NOT NULL REFERENCES campaigns(id),
    patient_id          BIGINT,
    lead_id             BIGINT,
    channel             VARCHAR(30)  NOT NULL,
    status              VARCHAR(30)  NOT NULL DEFAULT 'QUEUED', -- QUEUED, SENT, DELIVERED, OPENED, CLICKED, BOUNCED, UNSUBSCRIBED, FAILED
    provider_message_id VARCHAR(200),
    delivered_at        TIMESTAMP WITH TIME ZONE,
    opened_at           TIMESTAMP WITH TIME ZONE,
    clicked_at          TIMESTAMP WITH TIME ZONE,
    bounced_at          TIMESTAMP WITH TIME ZONE,
    unsubscribed_at     TIMESTAMP WITH TIME ZONE,
    failure_reason      VARCHAR(500),
    idempotency_key     VARCHAR(100) UNIQUE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_delivery_recipient CHECK (patient_id IS NOT NULL OR lead_id IS NOT NULL)
);
CREATE INDEX IF NOT EXISTS idx_campaign_deliveries_campaign ON campaign_deliveries(campaign_id, status);
CREATE INDEX IF NOT EXISTS idx_campaign_deliveries_patient ON campaign_deliveries(patient_id);

-- ─── Communication History ───────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS communication_history (
    id                  BIGSERIAL PRIMARY KEY,
    patient_id          BIGINT,
    lead_id             BIGINT,
    campaign_id         BIGINT,
    template_id         BIGINT,
    channel             VARCHAR(30)  NOT NULL,
    direction           VARCHAR(20)  NOT NULL DEFAULT 'OUTBOUND', -- OUTBOUND, INBOUND
    event_type          VARCHAR(50)  NOT NULL, -- SENT, DELIVERED, OPENED, CLICKED, REPLIED, OPT_OUT, COMPLAINT, CALL_NOTE
    content_summary     VARCHAR(500), -- template name/subject only, NO PHI
    operator_id         BIGINT,
    consent_state       VARCHAR(20),
    branch_id           BIGINT,
    provider_message_id VARCHAR(200),
    event_timestamp     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_comm_history_patient ON communication_history(patient_id, event_timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_comm_history_lead ON communication_history(lead_id, event_timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_comm_history_campaign ON communication_history(campaign_id);

-- ─── Campaign Automations ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS campaign_automations (
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(200) NOT NULL,
    trigger_type        VARCHAR(50)  NOT NULL, -- NEW_LEAD, INACTIVE_PATIENT, MISSED_APPOINTMENT, BIRTHDAY, PREVENTIVE_CARE, MEMBERSHIP_RENEWAL, REFERRAL_COMPLETION, FEEDBACK_REQUEST, ABANDONED_BOOKING, POST_VISIT
    eligibility_criteria JSONB       NOT NULL DEFAULT '{}',
    delay_minutes       INT          NOT NULL DEFAULT 0,
    channel             VARCHAR(30)  NOT NULL,
    template_id         BIGINT,
    stop_conditions     JSONB        NOT NULL DEFAULT '[]',
    frequency_cap       INT          NOT NULL DEFAULT 1,
    quiet_hours_start   TIME,
    quiet_hours_end     TIME,
    branch_id           BIGINT,
    status              VARCHAR(30)  NOT NULL DEFAULT 'DRAFT', -- DRAFT, APPROVED, ACTIVE, PAUSED, ARCHIVED
    version             INT          NOT NULL DEFAULT 1,
    approved_by         BIGINT,
    approved_at         TIMESTAMP WITH TIME ZONE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- ─── Automation Enrollments ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS automation_enrollments (
    id              BIGSERIAL PRIMARY KEY,
    automation_id   BIGINT      NOT NULL REFERENCES campaign_automations(id),
    patient_id      BIGINT,
    lead_id         BIGINT,
    status          VARCHAR(30) NOT NULL DEFAULT 'ENROLLED', -- ENROLLED, SENT, COMPLETED, STOPPED, CANCELLED
    enrolled_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    send_after      TIMESTAMP WITH TIME ZONE,
    completed_at    TIMESTAMP WITH TIME ZONE,
    stop_reason     VARCHAR(200),
    CONSTRAINT uq_automation_enrollment UNIQUE (automation_id, patient_id, lead_id)
);
CREATE INDEX IF NOT EXISTS idx_auto_enroll_automation ON automation_enrollments(automation_id, status);

-- ─── NPS Surveys ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS nps_surveys (
    id                BIGSERIAL PRIMARY KEY,
    appointment_id    BIGINT,
    order_id          BIGINT,
    patient_id        BIGINT       NOT NULL,
    branch_id         BIGINT,
    service_id        BIGINT,
    doctor_id         BIGINT,
    sent_at           TIMESTAMP WITH TIME ZONE,
    completed_at      TIMESTAMP WITH TIME ZONE,
    status            VARCHAR(30)  NOT NULL DEFAULT 'PENDING', -- PENDING, SENT, COMPLETED, EXPIRED, SUPPRESSED
    idempotency_key   VARCHAR(100) NOT NULL UNIQUE, -- prevents duplicate surveys per event
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_nps_patient ON nps_surveys(patient_id);

-- ─── NPS Responses ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS nps_responses (
    id                  BIGSERIAL PRIMARY KEY,
    survey_id           BIGINT        NOT NULL REFERENCES nps_surveys(id),
    nps_score           INT           CHECK (nps_score BETWEEN 0 AND 10),
    rating              INT           CHECK (rating BETWEEN 1 AND 5),
    comments            TEXT,
    category            VARCHAR(100),
    escalation_status   VARCHAR(30)   NOT NULL DEFAULT 'NONE', -- NONE, ESCALATED, RESOLVED
    escalated_to        BIGINT,
    resolved_at         TIMESTAMP WITH TIME ZONE,
    resolution_notes    TEXT,
    submitted_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_survey_response UNIQUE (survey_id)
);
