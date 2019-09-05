import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class Vue3ToolsService {
    public extractMediumStringFromString(Noun: String, charac1: string, charac2: string): string {
        let k0: number;
        let k1: number;
        for (let k = 0; k < Noun.length; k++) {
            if (Noun.charAt(k) === String(charac1).valueOf()) {
                k0 = k;
            }
            if (Noun.charAt(k) === String(charac2).valueOf()) {
                k1 = k;
            }
        }
        return Noun.slice(k0 + 1, k1);
    }

    public TriDateTable(table: any[]) {
        table.sort(function(c, b) {
            return Number(new Date(c)) - Number(new Date(b));
        });
        return table;
    }

    public ChangeFormatDate(table: any[]) {
        for (let j = 0; j < table.length; j++) {
            table[j] = new Date(table[j]);
        }
        return table;
    }

    public deleteElementsinTable(table: any[]) {
        for (let i = 0; i < table.length; i++) {
            for (let j = i + 1; j < table.length; j++) {
                if (table[i] === table[j]) {
                    table.splice(j, 1);
                    j = j - 1;
                }
            }
        }
        return table;
    }

    public deleteElementsinTableById(table: any[]) {
        for (let i = 0; i < table.length; i++) {
            for (let j = i + 1; j < table.length; j++) {
                if (table[i]['id'] === table[j]['id']) {
                    table.splice(j, 1);
                    j = j - 1;
                }
            }
        }
        return table;
    }

    public triBynomCollaborator(a: any, b: any): number {
        if (a.nomCollaborator < b.nomCollaborator) {
            return -1;
        } else if (a.nomCollaborator === b.nomCollaborator) {
            return 0;
        } else {
            return 1;
        }
    }

    public triBynameProject(a: any, b: any): number {
        if (a.nameProject < b.nameProject) {
            return -1;
        } else if (a.nameProject === b.nameProject) {
            return 0;
        } else {
            return 1;
        }
    }
}
