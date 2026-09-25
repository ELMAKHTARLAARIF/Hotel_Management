ALTER TABLE reservations
    ADD COLUMN tax_amount NUMERIC(10, 2) NOT NULL DEFAULT 0;

ALTER TABLE reservations
    ADD CONSTRAINT chk_reservation_tax
        CHECK (tax_amount >= 0);