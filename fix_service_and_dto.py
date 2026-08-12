import re

with open('backend/src/main/java/com/healthcare/clinic/appointment/service/AppointmentService.java', 'r') as f:
    content = f.read()

replacement = """
    @Transactional(readOnly = true)
    public List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto> getAllTodayAppointments() {
        ZonedDateTime startOfDay = ZonedDateTime.now().toLocalDate().atStartOfDay(java.time.ZoneId.systemDefault());
        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto> dtos = appointmentRepository.findAllAppointmentsToday(startOfDay, endOfDay);
        
        // Inject token numbers
        for (com.healthcare.clinic.appointment.dto.AppointmentResponseDto dto : dtos) {
            queueTokenRepository.findByAppointmentId(dto.getId()).stream().findFirst()
                .ifPresent(token -> dto.setTokenNumber(token.getTokenNumber()));
        }
        return dtos;
    }
"""

content = re.sub(r'@Transactional\(readOnly = true\)\s+public List<com\.healthcare\.clinic\.appointment\.dto\.AppointmentResponseDto> getAllTodayAppointments\(\) \{.*?\n    \}', replacement.strip(), content, flags=re.DOTALL)

with open('backend/src/main/java/com/healthcare/clinic/appointment/service/AppointmentService.java', 'w') as f:
    f.write(content)

with open('backend/src/main/java/com/healthcare/clinic/appointment/dto/AppointmentResponseDto.java', 'r') as f:
    dto_content = f.read()

if 'private Integer tokenNumber;' not in dto_content:
    dto_content = dto_content.replace('private ZonedDateTime createdAt;', 'private ZonedDateTime createdAt;\n    private Integer tokenNumber;')
    
with open('backend/src/main/java/com/healthcare/clinic/appointment/dto/AppointmentResponseDto.java', 'w') as f:
    f.write(dto_content)

