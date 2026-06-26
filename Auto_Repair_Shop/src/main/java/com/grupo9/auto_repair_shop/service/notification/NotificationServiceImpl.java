package com.grupo9.auto_repair_shop.service.notification;

import com.grupo9.auto_repair_shop.dto.request.notification.NotificationRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.notification.NotificationResponse;
import com.grupo9.auto_repair_shop.entity.notification.Notification;
import com.grupo9.auto_repair_shop.entity.user.User;
import com.grupo9.auto_repair_shop.entity.workorder.WorkOrder;
import com.grupo9.auto_repair_shop.enums.NotificationChannel;
import com.grupo9.auto_repair_shop.exception.ForbiddenException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.notification.NotificationMapper;
import com.grupo9.auto_repair_shop.repository.notification.NotificationRepository;
import com.grupo9.auto_repair_shop.repository.user.UserRepository;
import com.grupo9.auto_repair_shop.repository.workorder.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final WorkOrderRepository workOrderRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse create(NotificationRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + request.getUserId()
                ));

        WorkOrder workOrder = null;
        if (request.getWorkOrderId() != null) {
            workOrder = workOrderRepository.findById(request.getWorkOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Orden de trabajo no encontrada con id: " + request.getWorkOrderId()
                    ));
        }

        Notification notification = Notification.builder()
                .user(user)
                .workOrder(workOrder)
                .title(request.getTitle())
                .body(request.getBody())
                .channel(request.getChannel())
                .read(false)
                .sentAt(LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);

        return notificationMapper.toResponse(saved);
    }

    @Override
    public void createAutomatic(UUID userId, String title, String body, UUID workOrderId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + userId
                ));

        WorkOrder workOrder = null;
        if (workOrderId != null) {
            workOrder = workOrderRepository.findById(workOrderId)
                    .orElse(null);
        }

        Notification notification = Notification.builder()
                .user(user)
                .workOrder(workOrder)
                .title(title)
                .body(body)
                .channel(NotificationChannel.PUSH)
                .read(false)
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> findAll(int page, int size) {

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "sentAt")
        );

        Page<NotificationResponse> result = notificationRepository.findAll(pageRequest)
                .map(notificationMapper::toResponse);

        return PageResponse.from(result);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> findMyNotifications(String email, int page, int size) {

        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "sentAt")
        );

        Page<NotificationResponse> result = notificationRepository
                .findByUserEmail(email, pageRequest)
                .map(notificationMapper::toResponse);

        return PageResponse.from(result);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse findById(UUID id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notificación no encontrada con id: " + id
                ));

        return notificationMapper.toResponse(notification);
    }

    @Override
    public NotificationResponse markAsRead(UUID id, String email) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notificación no encontrada con id: " + id
                ));

        if (!notification.getUser().getEmail().equals(email)) {
            throw new ForbiddenException(
                    "Solo puedes marcar como leídas tus propias notificaciones"
            );
        }

        notification.setRead(true);

        Notification updated = notificationRepository.save(notification);

        return notificationMapper.toResponse(updated);
    }

    @Override
    public void markAllAsRead(String email) {

        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE,
                Sort.by(Sort.Direction.DESC, "sentAt"));

        notificationRepository.findByUserEmail(email, pageRequest)
                .getContent()
                .forEach(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }
}
