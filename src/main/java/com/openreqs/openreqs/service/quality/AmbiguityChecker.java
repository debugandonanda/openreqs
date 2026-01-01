package com.openreqs.openreqs.service.quality;

import com.openreqs.openreqs.dto.RequirementRequest;
import com.openreqs.openreqs.exception.RequirementViolationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * IREB Syllabus - Unidade 5.4: Documentação em Linguagem Natural
 * Verifica ambiguidades conforme o Handbook do IREB
 */
@Component
public class AmbiguityChecker implements QualityRule {

    // Palavras perigosas (subjetivas)
    private static final List<String> SUBJECTIVE_WORDS = Arrays.asList(
            "rápido", "lento", "fácil", "difícil", "simples", "complexo",
            "intuitivo", "amigável", "bonito", "feio", "melhor", "pior",
            "eficiente", "suficiente", "adequado"
    );

    // Quantificadores universais (geralmente problemáticos)
    private static final List<String> UNIVERSAL_QUANTIFIERS = Arrays.asList(
            "todos", "sempre", "nunca", "qualquer", "nenhum", "cada"
    );

    // Verbos imprecisos (falta de ação específica)
    private static final List<String> VAGUE_VERBS = Arrays.asList(
            "processar", "manipular", "tratar", "gerenciar", "lidar com"
    );

    @Override
    public void validate(RequirementRequest request) {
        String description = request.getDescription().toLowerCase();

        checkSubjectiveWords(description);
        checkUniversalQuantifiers(description);
        checkVagueVerbs(description);
        checkNominalizations(description);
        checkSuperlatives(description);
    }

    private void checkSubjectiveWords(String text) {
        for (String word : SUBJECTIVE_WORDS) {
            if (text.contains(word)) {
                throw new RequirementViolationException(
                        String.format("Ambiguidade detectada: '%s'. Esta palavra é subjetiva.", word),
                        "SUBJECTIVITY",
                        word,
                        "Substitua por métricas objetivas. Ex: 'rápido' → 'responder em menos de 2 segundos'"
                );
            }
        }
    }

    private void checkUniversalQuantifiers(String text) {
        for (String quantifier : UNIVERSAL_QUANTIFIERS) {
            Pattern pattern = Pattern.compile("\\b" + quantifier + "\\b");
            if (pattern.matcher(text).find()) {
                throw new RequirementViolationException(
                        String.format("Quantificador universal detectado: '%s'. Verifique se realmente não há exceções.", quantifier),
                        "UNIVERSAL_QUANTIFIER",
                        quantifier,
                        "Considere usar termos como 'na maioria dos casos', 'geralmente', ou especifique exceções"
                );
            }
        }
    }

    private void checkVagueVerbs(String text) {
        for (String verb : VAGUE_VERBS) {
            if (text.contains(verb)) {
                throw new RequirementViolationException(
                        String.format("Verbo vago detectado: '%s'. Ação não especificada.", verb),
                        "VAGUE_ACTION",
                        verb,
                        "Especifique a ação exata. Ex: 'processar' → 'validar, calcular e armazenar'"
                );
            }
        }
    }

    private void checkNominalizations(String text) {
        // Verifica substantivos derivados de verbos (terminações comções)
        Pattern nominalizationPattern = Pattern.compile(
                "\\b(\\w*)(ação|amento|imento|ção|são|tura|ência|ância)\\b",
                Pattern.CASE_INSENSITIVE
        );

        var matcher = nominalizationPattern.matcher(text);
        if (matcher.find()) {
            String nominalization = matcher.group();
            throw new RequirementViolationException(
                    String.format("Nominalização detectada: '%s'. Pode ocultar ações.", nominalization),
                    "NOMINALIZATION",
                    nominalization,
                    "Use a forma verbal. Ex: 'realização do cálculo' → 'o sistema deve calcular'"
            );
        }
    }

    private void checkSuperlatives(String text) {
        Pattern superlativePattern = Pattern.compile(
                "\\b\\w+(íssimo|érrimo|ílimo|zinho|inho)\\b|\\b(muito|pouco)\\s+\\w+\\b",
                Pattern.CASE_INSENSITIVE
        );

        var matcher = superlativePattern.matcher(text);
        if (matcher.find()) {
            String superlative = matcher.group();
            throw new RequirementViolationException(
                    String.format("Superlativo detectado: '%s'. É subjetivo.", superlative),
                    "SUPERLATIVE",
                    superlative,
                    "Especifique com números ou critérios objetivos"
            );
        }
    }

    @Override
    public String getRuleName() {
        return "Ambiguity Checker";
    }

    @Override
    public String getRuleDescription() {
        return "Verifica ambiguidades em requisitos documentados em linguagem natural conforme IREB";
    }
}