package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private TestCenterRepository testCenterRepository;
    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Test
        // TEST DE LA PREMIERE FONCTION
    void findAllByTestCenterEntityCode() {

        // Getting
        TestCenterEntity center1 = TestCenterEntity
                .builder()
                .code(TestCenterCode.PAR)
                .university("UGA")
                .city("Grenoble")
                .build();

        TestCenterEntity center2 = TestCenterEntity
                .builder()
                .code(TestCenterCode.DIJ)
                .university("CPGE")
                .city("Valence")
                .build();

        testCenterRepository.save(center1);
        testCenterRepository.save(center2);

        CandidateEntity candidate1 = CandidateEntity
                .builder()
                .firstname("basma")
                .lastname("morabit")
                .birthDate(LocalDate.of(2001, 11, 10))
                .hasExtraTime(true)
                .email("ddd@gmail.com")
                .phoneNumber("061890")
                .testCenterEntity(center1)
                .build();


        CandidateEntity candidate2 = CandidateEntity
                .builder()
                .firstname("loubna")
                .lastname("morabet")
                .birthDate(LocalDate.of(1999, 7, 23))
                .hasExtraTime(false)
                .email("alae@gmail.com")
                .phoneNumber("0611451")
                .testCenterEntity(center2)
                .build();

        CandidateEntity candidate3 = CandidateEntity
                .builder()
                .firstname("alae")
                .lastname("azzouzi")
                .birthDate(LocalDate.of(1999, 7, 23))
                .hasExtraTime(false)
                .email("lou@gmail.com")
                .phoneNumber("879625")
                .testCenterEntity(center2)
                .build();


        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);
        candidateRepository.save(candidate3);


        center1.setCandidateEntities(Set.of(candidate1));
        center1.setCandidateEntities(Set.of(candidate2,candidate3));


        // When
        Set<CandidateEntity> candidatesDIJ = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.DIJ);
        Set<CandidateEntity> candidatesPAR = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.PAR);

        // Then
        assertThat(candidatesDIJ).hasSize(2);
        assertThat(candidatesPAR).hasSize(1);

    }

    /**********************************************************************************************************************************************/
    // TEST DE LA DEUXIEME FONCTION
    @Test
    void findAllByCandidateEvaluationGridEntitiesGradeLessThan() {
        // GETTING
         CandidateEvaluationGridEntity candidateEvaluationGrid1 = CandidateEvaluationGridEntity
                 .builder()
                 .sheetNumber(1L)
                 .grade(3.55)
                 .submissionDate(LocalDateTime.of(2021,4,15,15,00,32))
                 .build() ;

         CandidateEvaluationGridEntity candidateEvaluationGrid2 = CandidateEvaluationGridEntity
                 .builder()
                 .sheetNumber(2L)
                 .grade(16.22)
                 .submissionDate(LocalDateTime.of(2024,4,5,19,59,43))
                 .build() ;

         candidateEvaluationGridRepository.save(candidateEvaluationGrid1) ;
         candidateEvaluationGridRepository.save(candidateEvaluationGrid2) ;

         CandidateEntity candidateA = CandidateEntity
                 .builder()
                 .firstname("abdnr")
                 .lastname("mora")
                 .birthDate(LocalDate.of(1999, 4, 13))
                 .hasExtraTime(true)
                 .email("salma@gmail.com")
                 .phoneNumber("0613978")
                 .candidateEvaluationGridEntities(new HashSet<>(Set.of(candidateEvaluationGrid1)))
                 .build() ;

         CandidateEntity candidateB = CandidateEntity
                 .builder()
                 .firstname("salma")
                 .lastname("bola")
                 .birthDate(LocalDate.of(2007, 5, 29))
                 .hasExtraTime(false)
                 .email("abdnr@gmail.com")
                 .phoneNumber("0611978")
                 .candidateEvaluationGridEntities(new HashSet<>(Set.of(candidateEvaluationGrid2)))
                 .build() ;

         candidateRepository.save(candidateA) ;
         candidateRepository.save(candidateB);

         // When
         Set<CandidateEntity> candidatesMoins10 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(3.33) ;
         Set<CandidateEntity> candidatesMoins19 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(16.55) ;

         // Then
         assertThat(candidatesMoins10).hasSize(0) ;
         assertThat(candidatesMoins19).hasSize(0) ;
    }

    /**********************************************************************************************************************************************/
    // TEST DE LA TROISIEME FONCTION
    @Test
    void findAllByHasExtraTimeFalseAndBirthDateBefore() {
        // Ajoutez les candidats nécessaires pour tous les scénarios de test
        // Getting
        CandidateEntity candidate1 = CandidateEntity
                .builder()
                .firstname("John")
                .lastname("LLLL")
                .birthDate(LocalDate.of(1995, 6, 15))
                .hasExtraTime(true)
                .phoneNumber("10234567")
                .email("LLLL@example.com")
                .build();

        candidateRepository.save(candidate1);

        // Test avec aucune correspondance
        // When
        Set<CandidateEntity> candidatesNoMatch = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(1980, 1, 1));

        // Then
        assertThat(candidatesNoMatch).isEmpty();

        // Test avec des candidats ayant des dates de naissance exactement égales à la limite
        // Getting
        CandidateEntity candidate2 = CandidateEntity
                .builder()
                .firstname("Jane")
                .lastname("YYYY")
                .birthDate(LocalDate.of(1990, 1, 1))
                .hasExtraTime(false)
                .phoneNumber("98768221")
                .email("YYYY@example.com")
                .build();

        candidateRepository.save(candidate2);

        // When
        Set<CandidateEntity> candidatesExactMatch = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(1990, 1, 1));

        // Then
        assertThat(candidatesExactMatch).hasSize(0);

        // Test avec des candidats ayant des dates de naissance après la limite
        // Getting
        CandidateEntity candidate3 = CandidateEntity
                .builder()
                .firstname("Alice")
                .lastname("XXXX")
                .birthDate(LocalDate.of(2000, 1, 1))
                .hasExtraTime(false)
                .phoneNumber("55512345")
                .email("XXXX@example.com")
                .build();

        candidateRepository.save(candidate3);

        // When
        Set<CandidateEntity> candidateAfterLimit = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(1995, 1, 1));

        // Then
        assertThat(candidateAfterLimit).hasSize(1);

        // Test qui va nous retourner plus d'1 candidat
        // Getting
        CandidateEntity candidate4 = CandidateEntity
                .builder()
                .firstname("Basma")
                .lastname("Alae")
                .birthDate(LocalDate.of(1999, 1, 1))
                .hasExtraTime(false)
                .phoneNumber("06112019")
                .email("basma@example.com")
                .build();

        candidateRepository.save(candidate4);

        // When
        Set<CandidateEntity> candidatesAfterLimit = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2001, 1, 1));

        // Then
        assertThat(candidatesAfterLimit).hasSize(3);
    }


}

