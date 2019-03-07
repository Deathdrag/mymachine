CREATE TABLE flash_drive (
        flash_id                     serial primary key,
        flash_type_id                integer,
        free_space                   integer,
        usable_space                 integer,
        used_space                   integer,
        is_floppy                    boolean,
        is_drive                     boolean,
        user_photo                   varchar(200),
        flash_files                  varchar(200),
        files_in_flash               varchar(1000)
    );
