#!/usr/bin/env python3
# Re-applies the BLOB patch to the Reladomo-generated OilLogDatabaseObjectAbstract.java.
#
# Why: Reladomo 17.1.4 has no jdbcType/jdbcDataType option on <Attribute> (verified against
# the reladomogen 17.1.4 bytecode: the Attribute parser only accepts name, javaType, nullable,
# primaryKey, precision, scale, readonly, identity, columnName, ...). The generator therefore
# emits stm.setNull(pos++, Types.VARBINARY) for the nullable byte[] attachmentData, but the
# Derby column ATTACHMENTDATA is a BLOB: the client rejects it (SQLState 22005), Mithra
# swallows it in zBatchInsertForSameSourceAttribute, and ORB saves fail silently.
#
# After ANY regeneration of the domain bundle, run this script BEFORE compiling:
#     python tools/repatch_blob.py
# It is idempotent: if the patch marker is already present it exits with "already patched".
# Exit codes: 0 = patched or already OK, 1 = patch pattern not found (generator output shape
# changed - fix this script by hand), 2 = generated file not found.
#
# See also: al.sybina.ebr.logbook.domain/reladomo/OilLog.xml (NOTE above attachmentData).

import re
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parent.parent
GEN = REPO / ("al.sybina.ebr.logbook.domain/relamodoGeneratedSources/al/sybina/ebr/"
              "logbook/domain/reladomo/OilLogDatabaseObjectAbstract.java")

MARKER = "PATCH: ATTACHMENTDATA is a BLOB column"

# matches the regenerated (unpatched) block:
#     if (data.isAttachmentDataNull())
#     {
#         stm.setNull(pos++, Types.VARBINARY);
UNPATCHED = re.compile(
    r"(if \(data\.isAttachmentDataNull\(\)\)[ \t]*\r?\n[ \t]*\{[ \t]*\r?\n)(\s*)"
    r"stm\.setNull\(pos\+\+,\s*(?:java\.sql\.)?Types\.VARBINARY\);")

PATCHED_BLOCK = (
    "\\1\\2// PATCH: ATTACHMENTDATA is a BLOB column (see OCEAN_LOGBOOK_OPERATION_OIL_LOG.ddl).\n"
    "\\2// Reladomo 17.1.4 emits setNull(Types.VARBINARY) for nullable byte[] but Derby 10.14\n"
    "\\2// rejects VARBINARY against a BLOB parameter (SQLState 22005) -> silent save failure.\n"
    "\\2// jdbcType is not a supported <Attribute> option in 17.1.4, so re-apply after regenerating.\n"
    "\\2stm.setNull(pos++, java.sql.Types.BLOB);")


def main() -> int:
    if not GEN.exists():
        print(f"ERROR: generated file not found:\n  {GEN}", file=sys.stderr)
        return 2
    text = GEN.read_text(encoding="utf-8")
    # the Maven build (domain pom, generateReladomo) applies the same fix without the marker comment
    if MARKER in text or "stm.setNull(pos++, java.sql.Types.BLOB);" in text:
        print("already patched:")
        print(f"  {GEN}")
        return 0
    new_text, n = UNPATCHED.subn(PATCHED_BLOCK, text)
    if n == 0:
        print("ERROR: expected setNull(..., Types.VARBINARY) block for isAttachmentDataNull() "
              "not found - the generator output changed; update this script.", file=sys.stderr)
        return 1
    GEN.write_text(new_text, encoding="utf-8")
    print(f"patched {n} occurrence(s):")
    print(f"  {GEN}")
    print("next: recompile the domain bundle (Eclipse Project > Build, or the Maven build).")
    return 0


if __name__ == "__main__":
    sys.exit(main())
