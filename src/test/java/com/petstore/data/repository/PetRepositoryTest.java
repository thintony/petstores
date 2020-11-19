package com.petstore.data.repository;

import com.petstore.data.model.Gender;
import com.petstore.data.model.Pet;
import com.petstore.data.model.Store;
import com.petstore.data.repository.PetRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@SpringBootTest
@Sql(scripts = {"classpath:db/insert.sql"})

class PetstoreApplicationTests {
    @Autowired
    PetRepository petRepository;

    @Autowired
    StoreRepository storeRepository;

    @BeforeEach
    void setUp(){

    }
    @Test
    public void WhenPetIsSaved_theReturnPetId(){
        Pet pet = new Pet();
        pet.setName("Jack");
        pet.setAge(2);
        pet.setBreed("Dog");
        pet.setColor("Black");
        pet.setPetSex(Gender.MALE);

        log.info("Pet instance before saving --> {}", pet);
        petRepository.save(pet);

        assertThat(pet.getId()).isNotNull();
        log.info("Pet instance before saving --> {}", pet);

    }

    @Test
    @Transactional
    @Rollback(value = false)
    void WhenStoreIsMappedToPet_theForeignKeyIsPresent() {
        Store store = new Store();
        store.setName("Pet sellers");
        store.setLocation("Yaba");
        store.setContactNo("090442436700");


        Pet pet = new Pet();
        pet.setName("Jack");
        pet.setAge(5);
        pet.setBreed("Dog");
        pet.setColor("Black");
        pet.setPetSex(Gender.MALE);

        log.info("Pet instance before saving --> {}", pet);

        pet.setStore(store);
        //map to store
        petRepository.save(pet);


        log.info("Pet instance before saving --> {}", pet);
        log.info("Pet instance before saving --> {}", store);
        //assert
        assertThat(pet.getId()).isNotNull();
        assertThat(store.getId()).isNotNull();
        assertThat(pet.getStore()).isNotNull();

    }


    @Test
    public void whenIAddPetsToAStore_thenICanFetchAListOfPetsFromStore(){
        //create store
        Store store = new Store();
        store.setName("Pet sellers");
        store.setLocation("Yaba");
        store.setContactNo("090442436700");

       //create 2 pets

        Pet jack = new Pet();
        jack.setName("Jack");
        jack.setAge(5);
        jack.setBreed("Dog");
        jack.setColor("Black");
        jack.setPetSex(Gender.MALE);
        jack.setStore(store);

        Pet sally = new Pet();
        sally.setName("Sally");
        sally.setAge(5);
        sally.setBreed("Dog");
        sally.setColor("Brown");
        sally.setPetSex(Gender.FEMALE);
        sally.setStore(store);

        log.info("Pet instance after adding --> {}",jack, sally);
        store.addPets(jack);
        store.addPets(sally);

        //save store
        storeRepository.save(store);

        log.info("store instance after saving --> {}",store);

        //assert for pet id
        assertThat(jack.getId()).isNotNull();

        //assert for pet id
        assertThat(sally.getId()).isNotNull();

        //assert that store has pets
        assertThat(store.getPetList()).isNotNull();

    }

    @Test
    public void updateExistingPetDetailTest(){
        //fetch a pet
        Pet sally = petRepository.findById(34).orElse(null);
        //assert the field

        assertThat(sally).isNotNull();
        assertThat(sally.getColor()).isEqualTo("brown");
        //update pet field

        sally.setColor("purple");

        //save pet
        petRepository.save(sally);
        log.info("After updating pet object --> {}",sally);
        //assert that update field has changed
        assertThat(sally.getColor()).isEqualTo("purple");
    }

    @Test
    public void  whenIdeletePetFromDataBase_thenPetIsDeleted(){
        //check if pet exists
        boolean sally = petRepository.existsById(35);

        //assert that pet exist
        assertThat(sally).isTrue();
        //delete pet
        petRepository.deleteById(35);
        //check if pet exists
        assertThat(petRepository.existsById(35)).isFalse();

    }

}

