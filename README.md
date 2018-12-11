# Applying Software Composition to Agile Backlogs

#Requirement

- Maven
- Java8 (Oracle)
- Docker
- Angular2 CLI

#Install

Execute the following commands : 


1. ./build.sh
2. docker-compose up

#Description

Agile projects rely on Product Backlog Items (PBI), (e.g., stories), and iterations (e.g., sprints) associated to backlogs to drive project implementation. Thanks to recent results obtained at Utrecht University with respect to natural language processing applied to user stories, we propose in this project to define a compositional model that will support the development team (e.g., product owner, developers) when interacting with backlogs and PBIs.

This project is en exploratory attempt to apply software composition mechanisms to the notion of agile backlog. The key-point here is to model classical action made on a backlog as composition operators: (i) addition of a new Product Backlog Item (PBI, e.g., a User story), (ii) extraction of a sprint backlog from a product backlog, (iii) withdrawal of a PBI from a backlog. Based on these operators, the idea is to identify properties associated to these operators (impact of PBI withdrawal, effort associated to the addition of a story) and provide feedback to product owners and development teams.

We consider for this project a Product Backlog as a set of User Stories, modelled as a (i) a persona, (ii) a mean, and (iii) an end. It is possible to use Natural Language Processing methods and tools (e.g., VisualNarrator) to extract a domain model from a set of stories. A domain model identifies the main concepts associated to the product, and the features associated to these concepts. For example, in the story `As a visitor, I can create an account so that the website remembers me`, the associated domain model identifies two entities `visitor` and `account`, and an action named `create` going from `Visitor` to `Account`.

When creating a backlog by adding stories one by one to a backlog, we are basically relying on an informal composition operator `(select: Story x Backlog -> Backlog)`. Selecting a story into a sprint backlog also remove it from the product backlog `(remove: Story x Backlog -> Backlog)`. When decomposing an epic into a set of stories, we are also using a (de)composition operator `(slice: Epic -> Story*)`.

The objective of this project is to formalise and implement such operators, focusing on the impact of the compositions to the domain model. The resulting model can be audited (e.g., in terms of structural coverage, functional coverage, business value, development effort).
