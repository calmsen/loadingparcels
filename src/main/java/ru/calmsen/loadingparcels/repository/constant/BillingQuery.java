package ru.calmsen.loadingparcels.repository.constant;

public class BillingQuery {
    public static final String FIND_ALL = "SELECT id, \"user\", description, type, date, quantity, cost FROM public.billing WHERE \"user\" = ? AND date >= ? AND date <= ?";
    public static final String INSERT = "INSERT INTO public.billing(\"user\", description, type, date, quantity, cost) VALUES(?, ?, ?, ?, ?, ?)";
}
