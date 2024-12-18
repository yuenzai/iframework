package cn.ecosync.iframework.outbox.model;

import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EventConverter implements AttributeConverter<Event, String> {
    private final JsonSerde jsonSerde;

    public EventConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(Event event) {
        return jsonSerde.serialize(event);
    }

    @Override
    public Event convertToEntityAttribute(String s) {
        throw new UnsupportedOperationException();
    }
}
