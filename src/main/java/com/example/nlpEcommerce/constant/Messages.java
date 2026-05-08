package com.example.nlpEcommerce.constant;

public final class Messages {
    private Messages() {

    }

    // ========== USER MESSAGES ==========
    public static final String USER_REGISTERED_SUCCESS = "Kullanici kaydi basarili";
    public static final String USER_LOGIN_SUCCESS = "Giris basarili";
    public static final String USER_PROFILE_UPDATED = "Profil guncellendi";
    public static final String USER_DELETED = "Kullanici silindi";
    public static final String USER_NOT_FOUND = "Kullanici bulunamadi";
    public static final String EMAIL_ALREADY_EXISTS = "Bu e-posta adresi zaten kayitli: ";
    public static final String INVALID_EMAIL_OR_PASSWORD = "E-posta veya sifre hatali";
    public static final String INVALID_CURRENT_PASSWORD = "Mevcut sifre yanlis";

    // ========== REVIEW MESSAGES ==========
    public static final String REVIEW_ADDED = "Yorum eklendi";
    public static final String REVIEW_DELETED = "Yorum silindi";
    public static final String REVIEW_NOT_FOUND = "Yorum bulunamadi";
    public static final String PRODUCT_ALREADY_REVIEWED = "Bu urunu zaten degerendirdiniz";
    public static final String NO_PERMISSION_DELETE_REVIEW = "Bu yorumu silme yetkiniz yok";

    // ========== PRODUCT MESSAGES ==========
    public static final String PRODUCT_NOT_FOUND = "Urun bulunamadi";
    public static final String PRODUCT_CREATED = "Urun olusturuldu";
    public static final String PRODUCT_UPDATED = "Urun guncellendi";
    public static final String PRODUCT_DELETED = "Urun silindi";

    // ========== CATEGORY MESSAGES ==========
    public static final String CATEGORY_NOT_FOUND = "Kategori bulunamadi";
    public static final String CATEGORY_CREATED = "Kategori olusturuldu";
    public static final String CATEGORY_UPDATED = "Kategori guncellendi";
    public static final String CATEGORY_DELETED = "Kategori silindi";
    public static final String CATEGORY_DUPLICATE_NAME = "Bu isimde bir kategori zaten mevcut: ";

    // ========== ORDER MESSAGES ==========
    public static final String ORDER_NOT_FOUND = "Siparis bulunamadi";
    public static final String ORDER_CREATED = "Siparis olusturuldu";
    public static final String ORDER_STATUS_UPDATED = "Siparis durumu guncellendi";

    // ========== VALIDATION MESSAGES ==========
    public static final String VALIDATION_ERROR = "Dogrulama hatasi: ";
    public static final String SERVER_ERROR = "Sunucu hatasi: ";
}
