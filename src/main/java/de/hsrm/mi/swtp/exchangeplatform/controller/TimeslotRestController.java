package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.TimeslotRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.StudentService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeslotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A simple rest-controller which will handle any rest calls concerning {@link Timeslot Timeslots}.
 * Its base url is {@code '/api/v1/timeslot'}.
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/v1/timeslot")
public class TimeslotRestController {

    String BASEURL = "/api/v1/timeslot";
    TimeslotService timeslotService;
    StudentService studentService;

    /**
     * GET request handler.
     * Will handle any request GET request on {@code '/api/v1/timeslot'}.
     *
     * @return a JSON made up of a list containing all available {@link Timeslot Timeslots}.
     * If there are none will return an empty list.
     */
    @GetMapping
    public ResponseEntity<List<Timeslot>> getAllTimeslots() {
        return new ResponseEntity<>(timeslotService.getAll(), HttpStatus.OK);
    }

    /**
     * GET request handler.
     * Will handle any request GET request to {@code '/api/v1/timeslot/<id>'}.
     *
     * @param timeslotId is the id of an {@link Timeslot}.
     * @return {@link HttpStatus#OK} and the requested {@link Timeslot} instance if it is found. Otherwise will return {@link HttpStatus#BAD_REQUEST}
     */
    @GetMapping("/{timeslotId}")
    public ResponseEntity<Timeslot> getTimeTableById(@PathVariable Long timeslotId) {
        log.info(String.format("GET // " + BASEURL + "/%s", timeslotId));
        try {
            return new ResponseEntity<>(timeslotService.getById(timeslotId), HttpStatus.OK);
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * POST request handler.
     * Provides an endpoint to {@code '/api/v1/timeslot'} through which an {admin} may create a new {@link Timeslot} for a module.
     *
     * @param timeslot a new timeslot which is to be created and inserted into the database.
     * @return {@link HttpStatus#OK} and the created timeslot if was created successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
     */
    @PostMapping
    public ResponseEntity<Timeslot> createTimeslot(@RequestBody Timeslot timeslot,
                                                   BindingResult result) {
        log.info("POST // " + BASEURL);
        log.info(timeslot.toString());
        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            timeslotService.save(timeslot);
        } catch (NotCreatedException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            log.info(String.format("FAIL: Timeslot %s not created due to some error", timeslot.getId()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(timeslot, HttpStatus.OK);
    }

    /**
     * POST request handler.
     * Provides an endpoint to {@code '/api/v1/timeslot/join'} through which a user ({@link Student}) may join an {@link Timeslot}.
     *
     * @param timeslotRequestBody is an object which contains the id of an {@link Timeslot} and the matriculation number of a {@link Student}.
     * @return {@link HttpStatus#OK} and the updated timeslot if the user joined successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
     */
    @PostMapping("/join")
    public ResponseEntity<Timeslot> joinTimeslot(@RequestBody TimeslotRequestBody timeslotRequestBody,
                                                 BindingResult result) {
        log.info("POST // " + BASEURL + "/join");
        log.info(timeslotRequestBody.toString());
        if (result.hasErrors()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        try {
            timeslotService.addAttendeeToTimeslot(
                    timeslotRequestBody.getTimeslotId(),
                    this.studentService.getById(timeslotRequestBody.getMatriculationNumber())
            );
            return new ResponseEntity<>(
                    timeslotService.getById(timeslotRequestBody.getMatriculationNumber()),
                    HttpStatus.OK
            );
        } catch (StudentIsAlreadyAttendeeException | NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST request handler.
     * Provides an endpoint to {@code '/api/v1/timeslot/leave'} through which a user ({@link Student}) can lean an {@link Timeslot}.
     *
     * @param timeslotRequestBody is an object which contains the id of an {@link Timeslot} and the matriculation number of a {@link Student}.
     * @return {@link HttpStatus#OK} and the updated timeslot if the user left successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
     */
    @PostMapping("/leave")
    public ResponseEntity<Timeslot> leaveTimeslot(@RequestBody TimeslotRequestBody timeslotRequestBody,
                                                  BindingResult result) {
        log.info("POST // " + BASEURL + "/leave");
        log.info(timeslotRequestBody.toString());
        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            timeslotService.removeAttendeeFromTimeslot(
                    timeslotRequestBody.getTimeslotId(),
                    this.studentService.getById(timeslotRequestBody.getMatriculationNumber())
            );
            return new ResponseEntity<>(
                    timeslotService.getById(timeslotRequestBody.getMatriculationNumber()),
                    HttpStatus.OK
            );
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PATCH request handler.
     * Provides an endpoint to {@code '/api/v1/timeslot/<id>'} through which an {@link Timeslot} can be updated.
     *
     * @param timeslotId is the id of the timeslot which is to be updated/modified.
     * @param timeslot   is an object which contains the date of an the updated {@link Timeslot}.
     * @return {@link HttpStatus#OK} and the updated timeslot if the timeslot was updated successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
     */
    @PatchMapping("/{timeslotId}")
    public ResponseEntity<Student> updateTimeslot(@PathVariable Long timeslotId,
                                                  @RequestBody Timeslot timeslot,
                                                  BindingResult result) {
        log.info(String.format("PATCH // " + BASEURL + "/%s", timeslotId));
        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            timeslotService.update(timeslotId, timeslot);
            return new ResponseEntity<>(studentService.getById(timeslotId), HttpStatus.OK);
        } catch (NotUpdatedException e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    /**
     * DELETE request handler.
     * Provides an endpoint to {@code '/api/v1/timeslot/admin/<id>'} through which an {admin} may delete {@link Timeslot}.
     *
     * @param timeslotId the id of an timeslot which is to be deleted.
     * @return {@link HttpStatus#OK} if the timeslot was removed successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
     */
    @DeleteMapping("/admin/{timeslotId}")
    public ResponseEntity<Timeslot> deleteTimeslot(@PathVariable Long timeslotId) {
        log.info(String.format("DELETE // " + BASEURL + "/admin/s", timeslotId));

        try {
            timeslotService.delete(timeslotId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.info(String.format("FAIL: Timeslot %s not deleted due to error while parsing", timeslotId));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}