package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        fausseApi = mock(PokeBuildApi.class);
        bagarre = new Bagarre(fausseApi);

        // When
        when(fausseApi.recupererParNom("pikachu"))
                .thenReturn(CompletableFuture.completedFuture(new Pokemon("pikachu", "url1",
                        new Stats(1, 2)))
                );
    }

    @Test
    void erreur_si_pokemon_1_est_null() {
        // When
        when(fausseApi.recupererParNom(null))
            .thenReturn(CompletableFuture.failedFuture(new ErreurPokemonNonRenseigne("premier")));

        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer(null, "pikachu"));

        // Then
        assertThat(erreur)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void erreur_si_pokemon_2_est_null() {
        // When
        when(fausseApi.recupererParNom(null))
                .thenReturn(CompletableFuture.failedFuture(new ErreurPokemonNonRenseigne("premier")));

        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer("pikachu", null));

        // Then
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void erreur_si_pokemons_sont_null() {
        // When
        when(fausseApi.recupererParNom(null))
                .thenReturn(CompletableFuture.failedFuture(new ErreurPokemonNonRenseigne("premier")));

        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer(null, null));

        // Then
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void erreur_si_pokemons_sont_egaux() {
        // When
        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer("pikachu", "pikachu"));
        // Then
        assertThat(erreur).isInstanceOf(ErreurMemePokemon.class)
                .hasMessage("Impossible de faire se bagarrer un pokemon avec lui-meme");
    }

    @Test
    void erreur_si_pokemons_sont_vides() {
        // When
        when(fausseApi.recupererParNom(""))
                .thenReturn(CompletableFuture.failedFuture(new ErreurPokemonNonRenseigne("premier")));

        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer("",""));

        // Then
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void erreur_si_pokemon_1_est_vide() {
        // When
        when(fausseApi.recupererParNom(""))
                .thenReturn(CompletableFuture.failedFuture(new ErreurPokemonNonRenseigne("premier")));
        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer("", "pikachu"));
        // Then
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void erreur_si_pokemon_2_est_vide() {
        // When
        when(fausseApi.recupererParNom(null))
                .thenReturn(CompletableFuture.failedFuture(new ErreurPokemonNonRenseigne("second")));

        Throwable erreur = Assertions.catchThrowable(() -> bagarre.demarrer("pikachu", ""));
        // Then
        assertThat(erreur).isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void est_vainqueur_si_pokemon1_plus_fort_que_pokemon2() {
        // When
        when(fausseApi.recupererParNom("tortank"))
                .thenReturn(CompletableFuture.completedFuture(new Pokemon("tortank", "url2",
                        new Stats(0, 13)))
                );

        var futurVainqueur = bagarre.demarrer("pikachu","tortank");

        // Then
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies(pokemon -> {
                    assertThat(pokemon.getNom())
                            .isEqualTo("pikachu"); // autres assertions...
                });
    }
}