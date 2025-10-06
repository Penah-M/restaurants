README — Restaurant Service (GitHub Version)

Bu README sənədi RestaurantImpl servisi üçün GitHub layihəsi üçün uyğun formatda hazırlanıb. Servis Restaurant məlumatlarını yaratmaq, oxumaq və cache vasitəsilə sürətlə təmin etmək üçün hazırlanıb. Kafka event göndərmə və Redis cache inteqrasiyası mövcuddur.

Table of Contents

Məqsəd

Servis Sinifi

Əsas Metodlar

save

findById

findAll

mapToResponse

Cache və Kafka Integrasiyası

Logging

Tövsiyələr

Məqsəd

RestaurantImpl servisi aşağıdakı funksiyaları təmin edir:

Məlumat əlavə etmək (save)

ID üzrə məlumat əldə etmək (findById)

Bütün məlumatları əldə etmək (findAll)

Redis cache istifadə edərək performansı artırmaq

Kafka ilə event göndərmək

Servis Sinifi

RestaurantImpl sinifi RestaurantService interfeysini implement edir və aşağıdakı komponentləri istifadə edir:

RestaurantRepository — JPA vasitəsilə database əməliyyatları

CacheUtil — Redis cache üçün util sinifi

KafkaTemplate<String, RestaurantEvent> — Kafka event göndərmə

Annotasiyalar

@Service — Spring service komponenti

@RequiredArgsConstructor — final sahələr üçün avtomatik konstruktor

@FieldDefaults(level = PRIVATE, makeFinal = true) — sahələri private final edir

Əsas Metodlar
save(RestaurantRequest request)

Yeni RestaurantEntity yaradır və database-ə saxlayır

Kafka event göndərir (RestaurantEvent) — mövzu: restaurant

Redis cache-ə yazır (10 dəqiqə müddətinə)

RestaurantResponse qaytarır

findById(Long id)

Redis cache-dən əvvəlcə oxuyur

Əgər cache boşdursa, database-dən oxuyur və cache-ə yazır

RestaurantResponse qaytarır

findAll()

Redis cache-dən bütün məlumatları oxuyur

Əgər cache boşdursa, database-dən oxuyur və cache-ə yazır

List<RestaurantResponse> qaytarır

mapToResponse(RestaurantEntity entity)

RestaurantEntity-ni RestaurantResponse-ə çevirir

Cache və Kafka Integrasiyası
Redis Cache

CacheUtil sinifi Redis ilə əlaqəni təmin edir

saveRestaurant(id, entity, 10L, ChronoUnit.MINUTES) — məlumatı 10 dəqiqə cache-də saxlayır

getRestaurant(id) və getAllRestaurants() — cache-dən məlumatı oxumaq üçün

Kafka Event

Kafka mövzusu: restaurant

Event tipi: RestaurantEvent

Göndərmə nümunəsi:

RestaurantEvent event = RestaurantEvent.builder()
.name(restaurantEntity.getName())
.address(restaurantEntity.getAddress())
.build();


kafkaTemplate.send("restaurant", event);
Logging

Redisdən oxunduqda və databazadan oxunduqda konsolda mesaj çap edilir:

"Redisden oxunur melumat.."

"DATA BAZADAN oxuyub cache yazildi"

Tövsiyələr

Cache müddəti və CronJob-lar layihənizə görə tənzimlənə bilər

Kafka event listener-lər əlavə edərək async mesajlar işləyə bilər

Redis və Kafka konfigurasiya parametrlərini application.yml və ya application.properties-də saxlayın

Service metodları üçün proper exception handling (NotFoundException) mövcuddur

Bu README, GitHub layihəsi üçün formatlaşdırılıb və layihəni başa düşmək, servis metodlarından düzgün istifadə etmək üçün əsas məlumatları ehtiva edir.