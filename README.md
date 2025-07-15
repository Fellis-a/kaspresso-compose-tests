# Автотесты для приложения Meowle

Проект содержит автотесты для мобильного приложения Meowle, написанные с использованием Kaspresso + WireMock.

## Структура проекта

Реализовано 10 автотестов - по 5 для каждого UI подхода:

**View-тесты:**
- SearchCatTest.kt - Поиск котиков
- SearchCatToDetailTest.kt - Переход из поиска в детали
- LikeTopDislikedCatTest.kt - Лайк топового котика
- AddCatTest.kt - Добавление котика
- EditCatTest.kt - Редактирование котика

**Compose-тесты:**
- ComposeSearchCatTest.kt - Поиск котиков
- ComposeSearchCatToDetailTest.kt - Переход из поиска в детали
- ComposeLikeTopDislikedCatTest.kt - Лайк топового котика
- ComposeAddCatTest.kt - Добавление котика
- ComposeEditCatTest.kt - Редактирование котика

## Как запустить тесты

1. Открыть проект в IntelliJ IDEA
2. Подключить Android устройство или запустить эмулятор
3. Перейти в папку `app/src/androidTest/java/ru/tinkoff/fintech/meowle/`
4. Открыть нужный тестовый файл (например, `SearchCatTest.kt`)
5. Запустить тест с помощью зелёной кнопки или ПКМ → Run

