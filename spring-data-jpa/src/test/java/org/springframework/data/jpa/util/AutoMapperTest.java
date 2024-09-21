package org.springframework.data.jpa.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.jpa.mapping.JpaPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.context.PersistentEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// DTO 클래스 정의
class UserDto {
    private String name;
    private int age;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

// Entity 클래스 정의
class UserEntity {
    private String name;
    private int age;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class AutoMapperTest {

    @Test
    void testMapEntityToDto() throws Exception {
        // Given
        UserEntity entity = new UserEntity();
        entity.setName("John");
        entity.setAge(30);

        MappingContext<JpaPersistentEntity<?>, ?> mappingContext = Mockito.mock(MappingContext.class);
        JpaPersistentEntity<UserEntity> persistentEntity = Mockito.mock(JpaPersistentEntity.class);
        JpaPersistentProperty nameProperty = Mockito.mock(JpaPersistentProperty.class);
        JpaPersistentProperty ageProperty = Mockito.mock(JpaPersistentProperty.class);

        // Stub the mapping context
        Mockito.when(mappingContext.getPersistentEntity(UserEntity.class)).thenReturn(persistentEntity);

        // Mock property behavior
        Mockito.when(persistentEntity.getPersistentProperty("name")).thenReturn(nameProperty);
        Mockito.when(persistentEntity.getPersistentProperty("age")).thenReturn(ageProperty);

        Mockito.when(nameProperty.getGetter()).thenReturn(UserEntity.class.getMethod("getName"));
        Mockito.when(ageProperty.getGetter()).thenReturn(UserEntity.class.getMethod("getAge"));

        // When
        UserDto dto = AutoMapper.mapEntityToDto(UserDto.class, entity, mappingContext);

        // Then
        assertEquals("John", dto.getName());
        assertEquals(30, dto.getAge());
    }

    @Test
    void testMapEntityToDto_NullEntity() throws Exception {
        // Given
        MappingContext<JpaPersistentEntity<?>, ?> mappingContext = Mockito.mock(MappingContext.class);

        // When
        UserDto dto = AutoMapper.mapEntityToDto(UserDto.class, null, mappingContext);

        // Then
        assertNull(dto);
    }

    @Test
    void testMapEntityToDto_NullMappingContext() {
        // Given
        UserEntity entity = new UserEntity();
        entity.setName("John");
        entity.setAge(30);

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                AutoMapper.mapEntityToDto(UserDto.class, entity, null)
        );
        assertEquals("MappingContext must not be null", exception.getMessage());
    }
}