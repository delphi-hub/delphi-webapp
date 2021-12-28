import { XMLParser } from "fast-xml-parser";

export class MavenAccess {
    async getArtifactVersion(groupId: string, artifactId: string, version: string): Promise<any> {
        const url = `/maven2/${groupId.replaceAll(".", "/")}/${artifactId
            }/${version}/${artifactId}-${version}.pom`;

        const pom = await fetch(url)
            .then((response) => response.text())
            .then((str) => {
                const parser = new XMLParser();
                return parser.parse(str).project;
            })
            .catch((reason) => console.error(reason));
        return pom;
    }

    async getArtifact(groupId: string, artifactId: string): Promise<any> {
        const url = `/maven2/${groupId.replaceAll(".", "/")}/${artifactId}/`;

        const artifacts = await fetch(url)
            .then((response) => response.text())
            .then((text) => {
                const parser = new DOMParser();
                return Array.from(parser.parseFromString(text, "text/html")
                                        .getElementById("contents")
                                        ?.querySelectorAll("A")!)
                                    .slice(1).map(a => a.textContent)
                                             .filter(link => link?.endsWith("/"))
                                             .map(link => link?.replace("/", ""));
            })
            .catch((reason) => console.error(reason));

        return artifacts;
    }

    async getGroup(groupId: string): Promise<any> {
        const url = `/maven2/${groupId.replaceAll(".", "/")}/`;

        const group = await fetch(url)
            .then((response) => response.text())
            .then((text) => {
                const parser = new DOMParser();
                return Array.from(parser.parseFromString(text, "text/html")
                                        .getElementById("contents")
                                        ?.querySelectorAll("A")!)
                                    .slice(1).map(a => a.textContent)
                                             .filter(link => link?.endsWith("/"))
                                             .map(link => link?.replace("/", ""));
            })
            .catch((reason) => console.error(reason));

        return group;
    }


}