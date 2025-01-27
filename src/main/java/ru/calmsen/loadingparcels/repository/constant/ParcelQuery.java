package ru.calmsen.loadingparcels.repository.constant;

public class ParcelQuery {
    public static final String FIND_ALL = "SELECT name, form, symbol FROM public.parcel LIMIT ? OFFSET ?";
    public static final String FIND_BY_NAME = "SELECT name, form, symbol FROM public.parcel WHERE name = ?;";
    public static final String INSERT = "INSERT INTO public.parcel (name, form, symbol) VALUES (?, ?, ?)";
    public static final String UPDATE = "UPDATE public.parcel SET form = ?, symbol = ? WHERE name = ?";
    public static final String DELETE = "DELETE FROM public.parcel WHERE name = ?";
}
