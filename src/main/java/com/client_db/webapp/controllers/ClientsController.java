package com.client_db.webapp.controllers;


import com.client_db.webapp.models.Client;
import com.client_db.webapp.models.NewClients;
import com.client_db.webapp.models.Note;
import com.client_db.webapp.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.ui.Model;
import com.client_db.webapp.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.client_db.webapp.service.ExcelExportService;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientsController {


    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private NoteService noteService;


    @GetMapping({"", "/"})
    public String getClients(Model model) {
        var clients = clientRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("clients", clients);

        return "clients/index";
    }

    @GetMapping("/create")
    public String createClient(Model model) {
        NewClients newClients = new NewClients();
        model.addAttribute("newClients", newClients);

        return "clients/create";
    }

    @PostMapping("/create")
    public String createClient(@Valid @ModelAttribute NewClients newClients, BindingResult result) {

        if (clientRepo.findByEmail(newClients.getEmail()) != null) {
            result.addError(
                    new FieldError("newClients", "email", newClients.getEmail()
                            , false, null, null,
                            "Email address is already in use" )
            );
        }

        if (result.hasErrors()) {
            return "clients/create";
        }

        Client client = new Client();
        client.setFirstName(newClients.getFirstName());
        client.setLastName(newClients.getLastName());
        client.setEmail(newClients.getEmail());
        client.setPhone(newClients.getPhone());
        client.setAddress(newClients.getAddress());
        client.setStatus(newClients.getStatus());
        client.setCreatedAt(new Date());

        clientRepo.save(client);

        return "redirect:/clients";
    }

    @GetMapping("/edit")
    public String editClient(Model model, @RequestParam int id) {
        Client client = clientRepo.findById(id).orElse(null);
        if (client == null) {
            return "redirect:/clients";
        }

        NewClients newClients = new NewClients();
        newClients.setFirstName(client.getFirstName());
        newClients.setLastName(client.getLastName());
        newClients.setEmail(client.getEmail());
        newClients.setPhone(client.getPhone());
        newClients.setAddress(client.getAddress());
        newClients.setStatus(client.getStatus());

        model.addAttribute("client", client);
        model.addAttribute("newClients", newClients);


        return "clients/edit";
    }

    @PostMapping("/edit")
    public String editClient(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute NewClients newClients,
            BindingResult result) {

        Client client = clientRepo.findById(id).orElse(null);
        if (client == null) {
            return "redirect:/clients";
        }

        if (result.hasErrors()) {
            model.addAttribute("client", client);
            return "clients/edit";
        }



        // Phone validation (only if not empty)
        String phone = newClients.getPhone();
        if (phone != null && !phone.isBlank()) {
            if (!phone.matches("^[+]?[0-9\\s-]+$")) {
                result.addError(new FieldError("newClients", "phone",
                        "Invalid format."));
                return "clients/edit";
            }
        }



        // Verify Duplicated email
        Client existingClient = clientRepo.findByEmail(newClients.getEmail());
        if (existingClient != null && existingClient.getId() != client.getId()) {
            result.addError(new FieldError("newClients", "email",
                    "Email already in use"));
            return "clients/edit";
        }

        // Update all fields (including phone, even if it's empty)
        client.setFirstName(newClients.getFirstName());
        client.setLastName(newClients.getLastName());
        client.setEmail(newClients.getEmail());
        client.setPhone(phone);

        clientRepo.save(client);

        return "redirect:/clients";
    }



    @GetMapping("/delete")
    public String deleteClient(@RequestParam int id) {

        Client client = clientRepo.findById(id).orElse(null);

        if (client != null) {
            clientRepo.delete(client);
        }

        return "redirect:/clients";
    }


    private final ClientService clientService;

    public ClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/search")
    public String searchClients(@RequestParam("keyword") String keyword, Model model) {
        List<Client> clients = clientService.searchClients(keyword);
        model.addAttribute("clients", clients);

        if (keyword == null || keyword.trim().isEmpty()) {
            return "redirect:/clients";
        }

        return "clients/index"; // Goes to index.html
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel() throws Exception {
        List<Client> clients = clientRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        ByteArrayOutputStream stream = excelExportService.exportClientsToExcel(clients);

        byte[] bytes = stream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("clients_export.xlsx").build());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

    }

    @GetMapping("/notes/fragment")
    public String getNotesFragment(@RequestParam Long id, Model model) {
        List<Note> notes = noteService.findByClientId(id);
        model.addAttribute("notes", notes);
        model.addAttribute("clientId", id);
        model.addAttribute("newNote", new Note());
        return "clients/notes :: content";
    }


    @PostMapping("/notes/add")
    public String addNote(@RequestParam Long clientId, @RequestParam String content) {
        Client client = clientRepo.findById(clientId.intValue()).orElse(null);
        if (client == null) return "redirect:/clients";

        Note note = new Note();
        note.setClient(client);
        note.setContent(content);
        noteService.saveNote(note);

        return "redirect:/clients"; // sau redirect înapoi în modal dacă vrei AJAX
    }

    @DeleteMapping("/notes/delete/{noteId}")
    @ResponseBody
    public ResponseEntity<?> deleteNote(@PathVariable Long noteId) {
        try {
            noteService.deleteNoteById(noteId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }


}
