-- ==============================================================================
-- Script de Atualização: Associação de Unidades, Ocorrências e Reservas ao Condomínio
-- Banco de Dados: PostgreSQL (Neon)
-- ==============================================================================

-- 1. Atualizar a tabela UNIDADES
-- Associa unidades pelo nome do condomínio e bloco correspondente:
UPDATE unidades u
SET condominio_id = c.id
FROM condominios c
WHERE u.condominio_id IS NULL
  AND LOWER(TRIM(u.condominio_nome)) = LOWER(TRIM(c.nome))
  AND (c.condominio_bloco IS NULL OR u.bloco ILIKE '%' || c.condominio_bloco || '%' OR c.condominio_bloco ILIKE '%' || u.bloco || '%');

-- Caso reste alguma unidade sem o bloco exato, associa pelo nome do condomínio:
UPDATE unidades u
SET condominio_id = c.id
FROM condominios c
WHERE u.condominio_id IS NULL
  AND LOWER(TRIM(u.condominio_nome)) = LOWER(TRIM(c.nome));


-- 2. Atualizar a tabela OCORRENCIAS
-- Associa ocorrências existentes pelo nome do condomínio:
UPDATE ocorrencias o
SET condominio_id = c.id
FROM condominios c
WHERE o.condominio_id IS NULL
  AND LOWER(TRIM(o.condominio_nome)) = LOWER(TRIM(c.nome));


-- 3. Atualizar a tabela RESERVAS
-- Associa reservas existentes pelo nome do condomínio:
UPDATE reservas r
SET condominio_id = c.id
FROM condominios c
WHERE r.condominio_id IS NULL
  AND LOWER(TRIM(r.condominio_nome)) = LOWER(TRIM(c.nome));


-- 4. Atualizar a tabela MORADORES
-- Associa moradores existentes pelo nome do condomínio e bloco:
UPDATE moradores m
SET condominio_id = c.id
FROM condominios c
WHERE m.condominio_id IS NULL
  AND LOWER(TRIM(m.condominio_nome)) = LOWER(TRIM(c.nome))
  AND (c.condominio_bloco IS NULL OR m.bloco ILIKE '%' || c.condominio_bloco || '%' OR c.condominio_bloco ILIKE '%' || m.bloco || '%');

UPDATE moradores m
SET condominio_id = c.id
FROM condominios c
WHERE m.condominio_id IS NULL
  AND LOWER(TRIM(m.condominio_nome)) = LOWER(TRIM(c.nome));


-- ==============================================================================
-- Verificação pós-execução:
-- ==============================================================================
SELECT 'unidades' AS tabela, COUNT(*) AS pendentes_sem_condominio FROM unidades WHERE condominio_id IS NULL
UNION ALL
SELECT 'ocorrencias' AS tabela, COUNT(*) AS pendentes_sem_condominio FROM ocorrencias WHERE condominio_id IS NULL
UNION ALL
SELECT 'reservas' AS tabela, COUNT(*) AS pendentes_sem_condominio FROM reservas WHERE condominio_id IS NULL
UNION ALL
SELECT 'moradores' AS tabela, COUNT(*) AS pendentes_sem_condominio FROM moradores WHERE condominio_id IS NULL;
