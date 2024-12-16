package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;

import java.util.Iterator;

public abstract class ParcelParser {
    public abstract Parcel parse(Iterator<String> lines, String currentLine);
}
