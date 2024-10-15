package com.nerdware.roomy.features.offices.services;

import com.nerdware.roomy.domain.entities.Office;
import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.domain.entities.UserRole;
import com.nerdware.roomy.domain.exceptions.ResourceNotFoundException;
import com.nerdware.roomy.domain.exceptions.UnauthorizedException;
import com.nerdware.roomy.features.offices.dtos.requests.CreateOfficeDto;
import com.nerdware.roomy.features.offices.dtos.requests.UpdateOfficeDto;
import com.nerdware.roomy.features.offices.repositories.OfficeRepository;
import org.springframework.stereotype.Service;

@Service
public class OfficeService
{

    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository)
    {
        this.officeRepository = officeRepository;
    }

    public Office createOffice(CreateOfficeDto request, User caller) throws Exception
    {
        if(caller.getRole() != UserRole.Admin)
            throw new UnauthorizedException("You do not have permission to create offices");

        var office = new Office();
        office.setCapacity(request.capacity());
        office.setName(request.name());
        office.setLocation(request.location());

        return officeRepository.save(office);
    }

    public Iterable<Office> getAllOffices()
    {
        return officeRepository.findAll();
    }

    public Office getOfficeById(Integer id) throws Exception {
        var officeResult = officeRepository.findById(id);
        if(!officeResult.isPresent())
            throw new ResourceNotFoundException("Office not found");

        return officeResult.get();
    }

    public void deleteOffice(Integer id, User caller)
        throws Exception
    {
        if(caller.getRole() != UserRole.Admin)
            throw new UnauthorizedException("You do not have permission to delete an office");

        var officeResult = officeRepository.findById(id);
        if(!officeResult.isPresent())
            throw new ResourceNotFoundException("Office not found");

        officeRepository.deleteById(id);
    }

    public Office updateOffice(Integer id, UpdateOfficeDto request, User caller)
        throws Exception
    {
        if(caller.getRole() != UserRole.Admin)
            throw new UnauthorizedException("You do not have permission to update an office");

        var officeResult = officeRepository.findById(id);
        if(!officeResult.isPresent())
            throw new ResourceNotFoundException("Office not found");

        var office = officeResult.get();

        if(request.capacity() != null)
            office.setCapacity(request.capacity());

        if(request.location() != null)
            office.setLocation(request.location());

        if(request.name() != null)
            office.setName(request.name());

        return officeRepository.save(office);
    }
}
