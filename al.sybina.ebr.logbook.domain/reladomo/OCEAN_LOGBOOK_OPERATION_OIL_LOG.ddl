drop table if exists OCEAN_LOGBOOK_OPERATION_OIL_LOG;

create table OCEAN_LOGBOOK_OPERATION_OIL_LOG
(
    ID bigint not null,
    UUID varchar(255),
    SHIP_ID bigint,
    TYPE varchar(4) not null,
    CODELETTER varchar(1) not null,
    CODENUMBER varchar(8) not null,
    CONTENT varchar(32672) not null,
    SIGNATURE varchar(32672),
    GROUPUUID varchar(255) not null,
    ORDERINGROUP varchar(255),
    SIGNERRANK varchar(255),
    SIGNERNAME varchar(255),
    SIGNERSURNAME varchar(255),
    NOTE varchar(255),
    CORRECTIONNOTE varchar(255),
    MASTERSIGNERNAME varchar(255),
    MASTERSIGNERSURNAME varchar(255),
    MASTERSIGNATURE varchar(32672),
    SIGNATUREGROUPUUID varchar(255),
    MASTERSIGDATE date,
    ATTACHMENTNAME varchar(255),
    ATTACHMENTDATA BLOB,
    FROM_Z timestamp not null,
    THRU_Z timestamp not null,
    IN_Z timestamp not null,
    OUT_Z timestamp not null
);

