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
        !repositories[0]?.defaultBranch()?.isEmpty()
    }
}
