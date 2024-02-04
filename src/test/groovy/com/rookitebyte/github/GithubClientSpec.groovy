package com.rookitebyte.github

import spock.lang.Specification

class GithubClientSpec extends Specification {

    GithubClient githubClient

    def setup() {
        githubClient = GithubClient.create()
    }


    def "fetch repositories from existing users, expect repositories"() {
        when:
        var repositories = githubClient.fetchUsersRepositories("rookiebyte")
        then:
        !repositories[0]?.name()?.isEmpty()
    }

    def "fetch repository readme content"() {
        given:
        var repository = githubClient.fetchUserRepository("rookiebyte", "meerkat-patrol-android-tv")
        when:
        var content = githubClient.fetchReadme(repository)
        then:
        !content?.empty
        content.startsWith("---")
    }
}
