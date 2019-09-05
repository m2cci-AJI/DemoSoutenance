import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICollaborators } from 'app/shared/model/collaborators.model';

type EntityResponseType = HttpResponse<ICollaborators>;
type EntityArrayResponseType = HttpResponse<ICollaborators[]>;

@Injectable({ providedIn: 'root' })
export class CollaboratorsService {
    public resourceUrl = SERVER_API_URL + 'api/collaborators';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/collaborators';

    constructor(protected http: HttpClient) {}

    create(collaborators: ICollaborators): Observable<EntityResponseType> {
        return this.http.post<ICollaborators>(this.resourceUrl, collaborators, { observe: 'response' });
    }

    update(collaborators: ICollaborators): Observable<EntityResponseType> {
        return this.http.put<ICollaborators>(this.resourceUrl, collaborators, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICollaborators>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICollaborators[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICollaborators[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
