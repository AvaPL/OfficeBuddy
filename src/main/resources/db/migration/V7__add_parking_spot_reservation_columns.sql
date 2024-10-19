ALTER TABLE reservation
    ADD COLUMN parking_spot_id uuid REFERENCES parking_spot (id),
    ADD COLUMN plate_number    varchar;

ALTER TABLE reservation
    DROP CONSTRAINT reservation_overlap_excl;

ALTER TABLE reservation
    ADD CONSTRAINT desk_reservation_overlap_excl EXCLUDE USING gist(
        desk_id WITH =,
        tsrange(reserved_from, reserved_to, '[]') WITH &&
        ) WHERE (state IN ('Pending', 'Confirmed'));

ALTER TABLE reservation
    ADD CONSTRAINT parking_spot_reservation_overlap_excl EXCLUDE USING gist(
        parking_spot_id WITH =,
        tsrange(reserved_from, reserved_to, '[]') WITH &&
        ) WHERE (state IN ('Pending', 'Confirmed'));
