INSERT INTO tb_categoria (nome)
VALUES ('Informatica')
ON CONFLICT (nome) DO NOTHING;

INSERT INTO tb_categoria (nome)
VALUES ('Perifericos')
ON CONFLICT (nome) DO NOTHING;

INSERT INTO tb_categoria (nome)
VALUES ('Monitores')
ON CONFLICT (nome) DO NOTHING;

ALTER TABLE IF EXISTS tb_produto
ADD COLUMN IF NOT EXISTS id_categoria BIGINT;

UPDATE tb_produto
SET id_categoria = (
    SELECT id_categoria
    FROM tb_categoria
    WHERE nome = 'Informatica'
)
WHERE id_categoria IS NULL;

ALTER TABLE IF EXISTS tb_produto
ALTER COLUMN id_categoria SET NOT NULL;

ALTER TABLE IF EXISTS tb_produto
DROP CONSTRAINT IF EXISTS fk_produto_categoria;

ALTER TABLE IF EXISTS tb_produto
ADD CONSTRAINT fk_produto_categoria
FOREIGN KEY (id_categoria)
REFERENCES tb_categoria (id_categoria);
