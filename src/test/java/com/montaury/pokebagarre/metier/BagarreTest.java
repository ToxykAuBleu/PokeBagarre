package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
/*
* On peut tester la classe avec 5 tests en lien avec #démarrer():
* - Les 2 pokémons ont été saisies.
* - Le pokémon 1 est vide.
* - Le pokémon 2 est vide
* - Les 2 pokémons sont vides.
* - Les 2 pokémons sont identiques.
*/

class BagarreTest {
    PokeBuildApi fausseApi;
    Bagarre bagarre;

    @BeforeEach
    void setup() {
        // Given (For each)
        fausseApi = Mockito.mock(PokeBuildApi.class);
        bagarre = new Bagarre(fausseApi);
    }

    @Test
    void erreur_si_pokemon_1_est_null() {
        // When
        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer(null, "Pikachu"));
        // Then
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class)
            .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void erreur_si_pokemon_2_est_null() {
        // When
        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer("Pikachu", null));
        // Then
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void erreur_si_pokemons_sont_null() {
        // When
        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer(null, null));
        // Then
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void erreur_si_pokemons_sont_egaux() {
        // When
        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer("Pikachu", "Pikachu"));
        // Then
        assertThat(erreur).isInstanceOf(ErreurMemePokemon.class)
                .hasMessage("Impossible de faire se bagarrer un pokemon avec lui-meme");
    }

    @Test
    void pas_erreur_si_pokemons_sont_differents() {
        // When
        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer("Pikachu", "Tortank"));
        // Then
        assertThat(erreur).doesNotThrowAnyException();
    }
}