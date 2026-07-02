package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.BookingDto;
import com.growthmul.app.lawnmover_fs.dto.BookingSubmitRequest;
import com.growthmul.app.lawnmover_fs.entity.BookingRequest;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.entity.ServiceOffering;
import com.growthmul.app.lawnmover_fs.repository.BookingRepository;
import com.growthmul.app.lawnmover_fs.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepo;
    @Autowired private ServiceRepository serviceRepo;
    @Autowired private PublicTenantResolver tenantResolver;

    // ───────────────────────── PUBLIC ─────────────────────────

    public void submitBooking(String origin, BookingSubmitRequest req) {
        Company company = tenantResolver.resolve(origin);

        BookingRequest entity = new BookingRequest();
        entity.setFirstName(req.getFirstName());
        entity.setLastName(req.getLastName());
        entity.setEmail(req.getEmail());
        entity.setPhone(req.getPhone());
        entity.setAddress(req.getAddress());
        entity.setPreferredDate(req.getPreferredDate());
        entity.setNotes(req.getNotes());
        entity.setCompany(company);

        if (req.getServiceOfferingId() != null) {
            ServiceOffering offering = serviceRepo.findById(req.getServiceOfferingId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown service selected"));
            // Without this check, someone could submit a booking against a
            // service that belongs to a DIFFERENT business — silently
            // attributing the booking (and its revenue) to the wrong tenant.
            if (!offering.getCompany().getId().equals(company.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown service selected");
            }
            entity.setServiceOffering(offering);
        }

        bookingRepo.save(entity);
    }

    // ───────────────────────── ADMIN ─────────────────────────

    public List<BookingDto> getBookings(Long companyId) {
        return bookingRepo.findByCompanyIdOrderBySubmittedAtDesc(companyId)
                .stream().map(BookingDto::from).toList();
    }

    public void completeBooking(Long companyId, Long id) {
        BookingRequest req = ownedOrThrow(companyId, id);
        req.setCompleted(true);
        bookingRepo.save(req);
    }

    public void reopenBooking(Long companyId, Long id) {
        BookingRequest req = ownedOrThrow(companyId, id);
        req.setCompleted(false);
        bookingRepo.save(req);
    }

    public void deleteBooking(Long companyId, Long id) {
        BookingRequest req = ownedOrThrow(companyId, id);
        bookingRepo.delete(req);
    }

    private BookingRequest ownedOrThrow(Long companyId, Long id) {
        BookingRequest req = bookingRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (!req.getCompany().getId().equals(companyId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your booking request");
        }
        return req;
    }
}
