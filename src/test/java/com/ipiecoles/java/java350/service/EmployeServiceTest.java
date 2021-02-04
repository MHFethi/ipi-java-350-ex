package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

    @InjectMocks
    EmployeService employeService;

    @Mock
    EmployeRepository employeRepositoryMock;


    /*
     * Lorsque j'embauche un emploué qui s'appelle Jophn Doe, Titulaire d'un BTS,
     * à plein temps, il est bien stocké en bdd (verifier nom/prenom) avec le matricule
     * T003465 'le dernier matricule etant C00345) et un salaire de 1825.46
     * zmbauché à la date du jour
     */

    @Test
    void testEmbaucheEmployeTechnicienPleinTempsBTS() throws EmployeException {
        //GIVEN
        String nom = "John";
        String prenom = "Doe";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        /* Les mocks se font au niveau des "GIVEN" */
        // Mocker l'appel FindLastMAtricule
        Mockito.when(employeRepositoryMock.findLastMatricule()).thenReturn("00345");
        // Mocker l'appel FindByMatricule
        Mockito.when(employeRepositoryMock.findByMatricule("T00346")).thenReturn(null);


        //WHEN
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //THEN
        /*ArgumentCaptor nous permet de capturer un argument passé à une méthode afin de l'inspecter.
        C'est particulièrement utile lorsque nous ne pouvons pas accéder à l'argument en dehors de la méthode que nous souhaitons tester.*/
        ArgumentCaptor<Employe> captorEmploye = ArgumentCaptor.forClass(Employe.class);

        /* avec verify() on verifie que la methode save() est appelée mais aussi de recuperer l'argument qui a été passé dans la méthode save()'
        Puisque cette méthode attend un argument on lui assimile captorEmploye
        afin d'utiliser ma methode capture() pour recuperer l'employé qui a été passer en argument a cette méthode
         */
        Mockito.verify(employeRepositoryMock).save(captorEmploye.capture());

        // On instance un Employe et on lui donne en valeur ce qu'on recupere dans save() au moment de l'appel du service
        Employe employe = captorEmploye.getValue();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00346");
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
    }
}
