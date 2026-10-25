package al.sybina.ebr.log4j.configuration.file;

import al.sybina.ebr.log4j.configuration.file.FileContentReader.FileQueryResult;

class SuccessfulQueryResult implements FileQueryResult {

    private final int fileLineNumber;

    public SuccessfulQueryResult( int fileLineNumber ) {
        this.fileLineNumber = fileLineNumber;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public int fileLineNumber() {
        return fileLineNumber;
    }

    @Override
    public boolean isBeforeRange() {
        return false;
    }

    @Override
    public boolean isAfterRange() {
        return false;
    }

    @Override
    public String toString() {
        return "SuccessfulQueryResult{" +
                "fileLineNumber=" + fileLineNumber +
                '}';
    }
}
