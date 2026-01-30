# Conectar este projeto ao repositório GitHub

Este ZIP não inclui a pasta `.git` (o Git não costuma ser distribuído em ZIP). Para manter a conexão com o seu repositório oficial no GitHub, use uma destas duas formas.

## Opção A (recomendada): clonar o repositório e copiar os arquivos

1) Clone o repositório oficial:

```bash
git clone <URL_DO_SEU_REPOSITORIO_GITHUB> sistema-pagamentos
cd sistema-pagamentos
```

2) Copie o conteúdo deste projeto (pasta `sistema-pagamentos`) para dentro do repositório clonado, substituindo os arquivos.

3) Confirme se o Git continua conectado:

```bash
git remote -v
```

4) Commit e push:

```bash
git status
git add .
git commit -m "Fix: SOAP do Manager (JAX-WS) e ajustes de Swarm"
git push
```

## Opção B: inicializar um repositório novo e apontar para o GitHub

Se você ainda não tem o repositório clonado, pode iniciar aqui mesmo:

```bash
git init
git branch -M main
git remote add origin <URL_DO_SEU_REPOSITORIO_GITHUB>
git add .
git commit -m "Initial commit"
git push -u origin main
```
