package com.grupo1.cursosvulcano.service;
import com.grupo1.cursosvulcano.model.entity.Module;
import com.grupo1.cursosvulcano.repository.ModuleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {
        @Autowired
        private ModuleRepository moduleRepository;
    
        public List<Module> getAllModules() {
            return moduleRepository.findAll();
        } 

        public Module createModule(Module module) {
            return moduleRepository.save(module);
        }

}
