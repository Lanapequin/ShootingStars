//package eci.cvds.tdd.module.sportLoan.service;
//
//import eci.cvds.tdd.module.sportLoan.enums.EquipmentStatus;
//import eci.cvds.tdd.module.sportLoan.model.DTO.EquipmentStatusUpdateRequest;
//import eci.cvds.tdd.module.sportLoan.model.Equipment;
//import eci.cvds.tdd.module.sportLoan.repository.EquipmentRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ServiceEquipmentTest {
//
//    @InjectMocks
//    private ServiceEquipment serviceEquipment;
//
//    @Mock
//    private EquipmentRepository equipmentRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void addEquipment_ShouldSetAvailableTrueAndStatusGood() {
//        Equipment equipment = new Equipment();
//        equipment.setAvailable(false);
//        equipment.setStatus(EquipmentStatus.DAMAGED);
//
//        when(equipmentRepository.save(any(Equipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Equipment saved = serviceEquipment.addEquipment(equipment);
//
//        assertTrue(saved.isAvailable());
//        assertEquals(EquipmentStatus.GOODSTATUS, saved.getStatus());
//        verify(equipmentRepository).save(equipment);
//    }
//
//    @Test
//    void updateEquipmentStatus_ShouldUpdateStatus() {
//        Equipment equipment = new Equipment();
//        equipment.setId("eq1");
//        equipment.setStatus(EquipmentStatus.GOODSTATUS);
//
//        EquipmentStatusUpdateRequest request = new EquipmentStatusUpdateRequest();
//        request.setEquipmentId("eq1");
//        request.setNewStatus(EquipmentStatus.MAINTENANCE);
//
//        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));
//        when(equipmentRepository.save(any(Equipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Equipment updated = serviceEquipment.updateEquipmentStatus(request);
//
//        assertEquals(EquipmentStatus.MAINTENANCE, updated.getStatus());
//        verify(equipmentRepository).save(equipment);
//    }
//
//    @Test
//    void updateEquipmentStatus_ThrowsExceptionWhenNotFound() {
//        EquipmentStatusUpdateRequest request = new EquipmentStatusUpdateRequest();
//        request.setEquipmentId("eq1");
//        request.setNewStatus(EquipmentStatus.MAINTENANCE);
//
//        when(equipmentRepository.findById("eq1")).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            serviceEquipment.updateEquipmentStatus(request);
//        });
//
//        assertEquals("Equipment not found", exception.getMessage());
//    }
//
//    @Test
//    void getAvailableEquipment_ReturnsList() {
//        Equipment eq1 = new Equipment();
//        Equipment eq2 = new Equipment();
//
//        when(equipmentRepository.findByStatusAndAvailable(EquipmentStatus.GOODSTATUS, true))
//                .thenReturn(List.of(eq1, eq2));
//
//        List<Equipment> list = serviceEquipment.getAvailableEquipment();
//
//        assertEquals(2, list.size());
//        verify(equipmentRepository).findByStatusAndAvailable(EquipmentStatus.GOODSTATUS, true);
//    }
//
//    @Test
//    void disableEquipment_ShouldSetAvailableFalse() {
//        Equipment equipment = new Equipment();
//        equipment.setId("eq1");
//        equipment.setAvailable(true);
//
//        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));
//        when(equipmentRepository.save(any(Equipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        serviceEquipment.disableEquipment("eq1");
//
//        assertFalse(equipment.isAvailable());
//        verify(equipmentRepository).save(equipment);
//    }
//
//    @Test
//    void disableEquipment_ThrowsExceptionWhenNotFound() {
//        when(equipmentRepository.findById("eq1")).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            serviceEquipment.disableEquipment("eq1");
//        });
//
//        assertEquals("Equipment not found", exception.getMessage());
//    }
//
//    @Test
//    void enableEquipment_ShouldSetAvailableTrue() {
//        Equipment equipment = new Equipment();
//        equipment.setId("eq1");
//        equipment.setAvailable(false);
//
//        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));
//        when(equipmentRepository.save(any(Equipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        serviceEquipment.enableEquipment("eq1");
//
//        assertTrue(equipment.isAvailable());
//        verify(equipmentRepository).save(equipment);
//    }
//
//    @Test
//    void enableEquipment_ThrowsExceptionWhenNotFound() {
//        when(equipmentRepository.findById("eq1")).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            serviceEquipment.enableEquipment("eq1");
//        });
//
//        assertEquals("Equipment not found", exception.getMessage());
//    }
//
//    @Test
//    void getEquipmentById_ReturnsEquipment() {
//        Equipment equipment = new Equipment();
//        equipment.setId("eq1");
//
//        when(equipmentRepository.findById("eq1")).thenReturn(Optional.of(equipment));
//
//        Equipment found = serviceEquipment.getEquipmentById("eq1");
//
//        assertEquals(equipment, found);
//    }
//
//    @Test
//    void getEquipmentById_ThrowsExceptionWhenNotFound() {
//        when(equipmentRepository.findById("eq1")).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            serviceEquipment.getEquipmentById("eq1");
//        });
//
//        assertEquals("Equipment not found", exception.getMessage());
//    }
//}
