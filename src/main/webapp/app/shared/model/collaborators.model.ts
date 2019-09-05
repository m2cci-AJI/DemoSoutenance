import { IAffectations } from 'app/shared/model/affectations.model';

export const enum Skill {
    JAVA = 'JAVA',
    C = 'C',
    JAVASCRIPT = 'JAVASCRIPT',
    PHP = 'PHP',
    UML = 'UML'
}

export interface ICollaborators {
    id?: number;
    nomCollaborator?: string;
    prenomCollaborator?: string;
    trigramme?: string;
    email?: string;
    competencies?: Skill;
    affectations?: IAffectations[];
}

export class Collaborators implements ICollaborators {
    constructor(
        public id?: number,
        public nomCollaborator?: string,
        public prenomCollaborator?: string,
        public trigramme?: string,
        public email?: string,
        public competencies?: Skill,
        public affectations?: IAffectations[]
    ) {}
}
