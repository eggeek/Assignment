# Part B

Hi, I've just finished question 5 and compress it with the project, see more detail below.

- [file download](https://drive.google.com/open?id=0B6nkhBPF6bK_VktPVW11UGE2c28)

The file structure is like this:

    ├── Assumptions.md
    ├── Assumptions.pdf
    ├── Conceptual\ model(9312\ Assignment\ 2\ Part\ A).pdf
    ├── Logical.pdf
    ├── code
    │   ├── maintain_count.sql
    │   ├── output
    │   │   ├── schema_output.txt
    │   │   └── test_maintain_count_output.txt
    │   ├── schema.sql
    │   └── test_maintain_count.sql
    └── combine
        ├── code
        │   ├── maintain_count.sql
        │   ├── output
        │   │   ├── schema_output.txt
        │   │   └── test_maintain_count_output.txt
        │   ├── schema.sql
        │   └── test_maintain_count.sql
        ├── combine/

You should add your `*.sql` file in `combine/code/` , and add output under the `output` folder, you can see more detail in my scripts, and the outside `code` folder is used for validation.

Besides, you can add some assumptions in `Assumptions.md` and export `pdf` file if you want, or edit on [Google doc](https://docs.google.com/a/monash.edu/document/d/17iheqi88XEOMyhgESB8uPme5vRbJV2iMtf-QJ3zSSvQ/edit?usp=sharing)


# Modified Tables

## Vehicle

- availability: add this field & create check clause

## Driver

- lincence_number: add unique constrain

- clearance_level: add check clause

- availibility: add check clause

## Coursees_available

- course_name: add unique constrain

## Qualifications

- available_course: become primary key
