.PHONY: all compile test clean package help

MVN := mvn

all: compile test

compile:
	$(MVN) compile

test:
	$(MVN) test

clean:
	$(MVN) clean

package: compile
	$(MVN) package -DskipTests

help:
	@echo "Objetivos disponibles:"
	@echo "  make compile  - Compila el codigo fuente"
	@echo "  make test     - Compila y ejecuta los tests con JUnit"
	@echo "  make all      - Compila y ejecuta los tests (por defecto)"
	@echo "  make clean    - Elimina artefactos generados (directorio target/)"
	@echo "  make package  - Genera el JAR sin ejecutar tests"
