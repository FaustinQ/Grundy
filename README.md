# Comparaison d'Approches Algorithmiques – Jeu de Grundy

## 📚 Contexte

Ce projet a été réalisé dans le cadre de la **SAE S1.02 : Comparaison d’approches algorithmiques** en BUT Informatique à l’IUT de Vannes.  
L’objectif est de simuler le **jeu de Grundy**, une variante du jeu de Nim, en développant plusieurs versions d’un algorithme de résolution, puis en comparant leurs performances.

## 👨‍💻 Auteurs

- QUINTANE Faustin  
- TOUMELIN Benoît

---

## 🎯 Objectifs du projet

1. **Implémenter plusieurs versions** d’un programme jouant au jeu de Grundy (IA contre humain).
2. **Comparer** ces versions selon :
   - Le temps d’exécution
   - Le nombre de calculs effectués
3. **Analyser** les compromis entre simplicité d’un algorithme et performance.

---

## ⚙️ Versions implémentées

### 🔹 Version 0 – Brute force

| n  | Calculs | Temps (ns) |
|----|---------|------------|
| 23 | 38M     | 7s         |

> Complexité : **Θ(n⁷)**

---

### 🔹 Version 1 – Optimisation basique

| n  | Calculs | Temps (ns) |
|----|---------|------------|
| 23 | 111k    | 0.2s       |
| 29 | 10M     | 10s        |

> Complexité : **Θ(n⁵)**

---

### 🔹 Version 2 – Mémorisation

| n  | Calculs | Temps (ns) |
|----|---------|------------|
| 30 | 18k     | 0.09s      |

> Complexité : **Θ(n²)**

---

### 🔹 Version 3 – Optimisation avancée

| n   | Calculs | Temps (ns) |
|-----|---------|------------|
| 500 | 62k     | 0.15s      |

> Complexité : **Θ(n)**

---

### 🔹 Version 4 – Approche la plus efficace

| n    | Calculs | Temps (ns) |
|------|---------|------------|
| 2000 | 1956    | 0.019s     |

> Complexité : **Θ(n⁰․⁷⁶)**

---

## 📊 Analyse comparative

| Version | Complexité estimée | Temps pour n=500 |
|---------|--------------------|------------------|
| V0      | Θ(n⁷)              | ✖️ (trop lent)   |
| V1      | Θ(n⁵)              | ✖️ (trop lent)   |
| V2      | Θ(n²)              | ~3s              |
| V3      | Θ(n)               | ~0.15s           |
| V4      | Θ(n⁰․⁷⁶)           | ~0.02s           |

> L’amélioration de version en version est flagrante.  
> La **version 4** permet de traiter **n = 2000** en moins de **0,02 seconde**.

---

Projet réalisé par QUINTANE Faustin
BUT Informatique – IUT de Vannes
Module S1.02 – Comparaison d'approches algorithmiques
Année universitaire 2024-2025

---
